package luxmart_backend.services.impl;

import lombok.RequiredArgsConstructor;
import luxmart_backend.domain.User;
import luxmart_backend.dto.CartFullItemDto;
import luxmart_backend.dto.CartItemDto;
import luxmart_backend.dto.OrderPayDto;
import luxmart_backend.dto.UpdateCartItemDto;
import luxmart_backend.models.*;
import luxmart_backend.repositories.*;
import luxmart_backend.services.CartService;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final OrderDataRepository orderDataRepository;
    private final CategoryRepository categoryRepository;
    private final PicturesRepository picturesRepository;

    @Override
    public CartItemDto addItemToCart(CartItemDto cartItem) {
        if (cartItem.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        User user = userRepository.findById(cartItem.getUserId().intValue())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepository.findById(cartItem.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        Cart cart = cartRepository.findByProductAndUser(product, user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            newCart.setProduct(product);
            newCart.setOrderPay(true);
            newCart.setQuantity(0L);
            return cartRepository.save(newCart);
        });
        cart.setQuantity(cartItem.getQuantity()+cart.getQuantity());
        cart = cartRepository.save(cart);
        return new CartItemDto(cart.getId(), cart.getUser().getId(), cart.getProduct().getId(),
                cart.isOrderPay(), cart.getQuantity());
    }

    @Override
    public List<CartFullItemDto> getCarFulltByUserId(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Cart> cartItems = cartRepository.findByUser(user);

        return cartItems.stream().map(cartItem -> {
            var product = productRepository.findById(cartItem.getProduct().getId()).orElseThrow();
            var category = categoryRepository.findById(product.getCategory().getId()).orElseThrow();
            // Получаем список изображений для продукта

            List<String> imageUrls = picturesRepository.findUrlsByProductId(product.getId());
            return new CartFullItemDto(
                    cartItem.getId(),
                    cartItem.getUser().getId(),
                    cartItem.getProduct().getId(),
                    cartItem.isOrderPay(),
                    cartItem.getQuantity(),
                    product.getTitle(),
                    product.getPrice(),
                    product.getDescription(),
                    product.getCategory().getId(),
                    imageUrls,
                    category.getName()
            );
        }).collect(Collectors.toList());
    }

    @Override
    public void deleteItemFromCart(Long itemId) {
        Cart cart = cartRepository.findById(itemId).orElseThrow(() -> new RuntimeException("Cart not found"));
        cartRepository.delete(cart);
    }

    @Override
    public void clearCart() {
        cartRepository.deleteAll();
    }

    @Override
    public UpdateCartItemDto updateCartItem(Long itemId, UpdateCartItemDto cartItem) {
        Cart cart = cartRepository.findById(itemId).orElseThrow(() -> new RuntimeException("Cart item not found"));
        cart.setQuantity(cartItem.getQuantity());
        cart = cartRepository.save(cart);
        return new UpdateCartItemDto(cart.getQuantity());
    }

    @Override
    public Order addItemsToOrder(Integer userId, OrderPayDto orderPayDto) {
        // Получаем все элементы корзины для данного пользователя
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Cart> carts = cartRepository.findByUser(user);
        if (carts.isEmpty()) {
            throw new RuntimeException("Корзина пуста!");
        }

        // Считаем сумму только тех элементов, у которых isOrderPay() == true
        double totalSum = carts.stream()
                .filter(cart -> cart.isOrderPay())  // Оставляем только те, где isOrderPay() == true (Metod[ filter(cart -> cart.isOrderPay())]
                .mapToDouble(cart -> cart.getProduct().getPrice() * cart.getQuantity())
                .sum();

       if(totalSum>0){
           // Создаем новый заказ (данные берем с сайта оплаты)
           Order order = Order.builder()
                   .user(carts.get(0).getUser()) // Связываем заказ с пользователем
                   .absender(orderPayDto.getAbsender())
                   .post(orderPayDto.getPost())
                   .nameBank(orderPayDto.getNameBank())
                   .carte(orderPayDto.getCarte())
                   .dateOrder(LocalDateTime.now()) // Текущая дата и время
                   .sumOrder(totalSum)
                   .build();
           System.out.println("----------------------" + orderPayDto.getAbsender());
           orderRepository.save(order);
           // Создаем записи в таблице OrderData для каждого элемента корзины
           for (Cart cartItem : carts) {
               if(cartItem.isOrderPay()) {
                   OrderData orderData = OrderData.builder()
                           .order(order)
                           .product(cartItem.getProduct())
                           .title(cartItem.getProduct().getTitle())
                           .quantity(cartItem.getQuantity())
                           .price(cartItem.getProduct().getPrice())
                           .description(cartItem.getProduct().getDescription())
                           .build();
                   orderDataRepository.save(orderData);
               }
           }

           // Удаляем только те элементы корзины, которые оплачены (isOrderPay == true)
           carts.removeIf(cartItem -> !cartItem.isOrderPay());
           cartRepository.deleteAll(carts);  // Удаляем оставшиеся элементы корзины из базы
           return order;
       }
        return null;
    }

    @Override
    public CartItemDto paymentCartItem(Long itemId) {
        Cart cart = cartRepository.findById(itemId).orElseThrow(() -> new RuntimeException("Cart item not found"));
        cart.setOrderPay((cart.isOrderPay())?(false):(true));
        cart = cartRepository.save(cart);
        return new CartItemDto(cart.getId(), cart.getUser().getId(), cart.getProduct().getId(), cart.isOrderPay(), cart.getQuantity());
    }
}
