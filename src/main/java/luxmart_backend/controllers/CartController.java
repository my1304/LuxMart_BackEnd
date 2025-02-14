package luxmart_backend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import luxmart_backend.dto.CartFullItemDto;
import luxmart_backend.dto.OrderPayDto;
import luxmart_backend.models.Order;
import luxmart_backend.dto.CartItemDto;
import luxmart_backend.dto.UpdateCartItemDto;
import luxmart_backend.services.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Cart Controller", description = "Controller for managing cart items")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    @Operation(summary = "Add item to cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item added to cart successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping
    public ResponseEntity<CartItemDto> addItemToCart(@RequestBody @Valid CartItemDto cartItem) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(cartService.addItemToCart(cartItem));
    }
    /*
        @Operation(summary = "Get cart products by cart id")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "List of cart products returned successfully"),
                @ApiResponse(responseCode = "404", description = "No cart products found for cart")
        })
        @GetMapping("/{cart-id}")
        public ResponseEntity<List<CartProductDto>> getCartProductsByCartId(@PathVariable("cart-id") Long cartId) {
            List<CartProductDto> cartProducts = cartService.getCartProductsDtoByCartId(cartId);
            if (cartProducts.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.NO_CONTENT)
                        .body(cartProducts);
            }
            return ResponseEntity
                    .ok(cartProducts);
        }
    */
    @Operation(summary = "Get all cart's user items ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of cart items returned successfully"),
            @ApiResponse(responseCode = "404", description = "No items found in cart")
    })
    @GetMapping("/{user-id}")
    public ResponseEntity<List<CartFullItemDto>> getCarFulltByUserId(@PathVariable("user-id") Integer userId) {
        return ResponseEntity
                .ok(cartService.getCarFulltByUserId(userId));
    }


    @Operation(summary = "Post order und datas by user items ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of orders items returned successfully"),
            @ApiResponse(responseCode = "404", description = "No items found in order")
    })
    @PutMapping("/order/{user-id}")
    public ResponseEntity<Order> addItemsToOrder(@PathVariable("user-id") Integer userId,
                                                 @RequestBody @Valid OrderPayDto orderPayDto) {
        return ResponseEntity
                .ok(cartService.addItemsToOrder(userId, orderPayDto));

    }

    @Operation(summary = "Delete item from cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item deleted from cart successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @DeleteMapping("/{item-id}")
    public ResponseEntity<Void> deleteItemFromCart(@PathVariable("item-id") Long itemId) {
        cartService.deleteItemFromCart(itemId);
        return ResponseEntity
                .noContent()
                .build();
    }


    @Operation(summary = "Update cart item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart item updated successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PutMapping("/{item-id}")
    public ResponseEntity<UpdateCartItemDto> updateCartItem(@PathVariable("item-id") Long itemId, @RequestBody @Valid UpdateCartItemDto updateCartItem) {
        return ResponseEntity
                .ok(cartService.updateCartItem(itemId, updateCartItem));
    }

    @Operation(summary = "Clear cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart cleared successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart() {
        cartService.clearCart();
        return ResponseEntity
                .noContent()
                .build();
    }
    @Operation(summary = "Change Payment item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart Payment changed successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @GetMapping("/payment/{item-id}")
    public ResponseEntity<CartItemDto> paymentCartItem(@PathVariable("item-id") Long itemId) {
        return ResponseEntity
                .ok(cartService.paymentCartItem(itemId));
    }
}
