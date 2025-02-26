package luxmart_backend.services;

import luxmart_backend.dto.CartFullItemDto;
import luxmart_backend.dto.CartItemDto;
import luxmart_backend.dto.OrderPayDto;
import luxmart_backend.dto.UpdateCartItemDto;
import luxmart_backend.models.Order;

import java.util.List;

public interface CartService {

    CartItemDto addItemToCart(CartItemDto cartItem);

    List<CartFullItemDto> getCarFulltByUserId(Integer userId);

    void deleteItemFromCart(Long itemId);

    void clearCart();

    UpdateCartItemDto updateCartItem(Long itemId, UpdateCartItemDto cartItem);

    Order addItemsToOrder(Integer userId, OrderPayDto orderPayDto);

    CartItemDto paymentCartItem(Long itemId);
}
