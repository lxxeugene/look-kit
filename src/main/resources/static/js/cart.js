document.addEventListener("DOMContentLoaded", function() {
    const cartItemList = document.querySelector(".cart-item-list");

    $.ajax({
      type: "GET",
      url: "/cart/items",
      success: function(cartItems) {
        console.log("장바구니 데이터:", cartItems); // 데이터를 로깅하여 확인합니다.
        if (!Array.isArray(cartItems)) {
          cartItems = []; // 응답 데이터가 배열이 아니면 빈 배열로 초기화합니다.
        }

        cartItems.forEach(item => {
          const productPrice = item.productPrice;


          const cartItemHTML = `
            <div class="cart-item" data-cart-id="${item.cartId}">
              <div class="item-checkbox">
                <input type="checkbox" class="checkbox-input" checked />
              </div>
              <div class="item-thumbnail">
                <a href="/product/${item.productId}">
                  <img class="thumbnail-img" src="/images/products/0${item.productId}/${item.productId}_thumbnail.webp" alt="상품 이미지" />
                </a>
              </div>
              <div class="item-description">
                <div class="brand-name2">${item.brandName}</div>
                <div class="product-name">${item.productName}</div>
                <div class="product-variant">${item.quantity}개</div>
              </div>
              <div class="item-price-box">
                <div class="item-price">
                  <strong>${productPrice.toLocaleString()}원</strong>
                </div>
                <div class="updateditem-price">
                  <strong>${productPrice.toLocaleString()}원</strong>
                </div>
              </div>
              <div>
                <input class="item-quantity quantity-input" type="number" value="${item.quantity}" min="1" data-cart-id="${item.cartId}" />
              </div>
            </div>`;
          cartItemList.insertAdjacentHTML("beforeend", cartItemHTML);
        });
        updateTotalPrice();
      },
      error: function() {
        alert("장바구니 데이터를 불러오는 데 실패했습니다.");
      }
    });

    $(document).on("change", ".item-quantity", function () {
      const cartId = $(this).closest(".cart-item").data("cart-id");
      const newQuantity = parseInt($(this).val());

      if (newQuantity <= 0) {
        alert("수량은 1 이상이어야 합니다.");
        return;
      }

      $.ajax({
        type: "POST",
        url: "/cart/update",
        data: { cartId: cartId, quantity: newQuantity },
        success: function() {
          updateTotalPrice();
      },
        error: function() {
          alert("수량을 변경하는 데 실패했습니다.");
        }
      });
    });

    $("#delete-selected-button").click(function () {
      const selectedItems = $(".checkbox-input:checked").map(function() {
        return $(this).closest(".cart-item").data("cart-id");
      }).get();

      if (selectedItems.length === 0) {
        alert("선택된 항목이 없습니다.");
        return;
      }

      $.ajax({
        type: "POST",
        url: "/cart/delete",
        data: { cartIds: selectedItems },
        traditional: true,
        success: function() {
          selectedItems.forEach(cartId => {
            $(`[data-cart-id='${cartId}']`).remove();
          });
          updateTotalPrice();
          $(".checkbox-input").prop("checked", true);
        },
        error: function() {
          alert("선택된 상품을 삭제하는 데 실패했습니다.");
        }
      });
    });

    $(document).on("change", ".checkbox-input", function () {
      updateTotalPrice();
    });

    function updateTotalPrice() {
      let totalAmount = 0;
      let totalQuantity = 0;

      $(".cart-item").each(function () {
        const quantity = parseInt($(this).find(".item-quantity").val());
        const price = parseInt($(this).find(".item-price strong").text().replace(/\D/g, ""));

        if (!isNaN(quantity) && !isNaN(price)) {
          totalAmount += quantity * price;
          totalQuantity += quantity;
        }
      });

      $("#total-price").text(totalAmount.toLocaleString() + "원");
      $("#total-final-price").text(totalAmount.toLocaleString() + "원");
      $("#checkout-button span").text(`총 ${totalQuantity}개 / ${totalAmount.toLocaleString()}원 주문하기`);
    }

    $("#checkout-button").click(function() {
      location.href = "/cart/checkout";
    });
  });