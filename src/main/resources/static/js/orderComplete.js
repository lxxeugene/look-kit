$(document).ready(function () {
    const orderId = localStorage.getItem("latestOrderId");
    if (!orderId) {
      alert("주문 정보가 없습니다.");
      return;
    }

    $.ajax({
      type: "GET",
      url: `/order/complete/details/${orderId}`,
      success: function (responseData) {
        if (responseData) {
          const orderData = responseData.order;
          const items = responseData.orderDetails;

          if (orderData) {
            $('#order-id').text(orderData.orderId);

            if (items && Array.isArray(items)) {
              items.forEach(item => {
                $('#product-list').append(`
                  <div class="product-item">
                    <div class="product-price">
                      <div class="price-amount">${item.productPrice.toLocaleString()}원</div>
                    </div>
                    <div class="product-details-wrapper">
                      <div class="product-image-wrapper">
                        <img class="product-image" src="/images/products/0${item.productId}/${item.productId}_thumbnail.webp" alt="상품 이미지" />
                      </div>
                      <div class="product-description">
                        <div class="product-brand-name">${item.brandName}</div>
                        <div class="product-name">${item.productName}</div>
                        <div class="product-variant">${item.quantity}개</div>
                      </div>
                    </div>
                  </div>
                `);
              });
            } else {
              $('#product-list').append('<div>주문한 상품 정보가 없습니다.</div>');
            }

            $('#shipping-address-name').text(orderData.orderAddress);
            $('#shipping-recipient-name').text(orderData.orderAddressee);
            $('#shipping-phone-number').text(orderData.orderPhone);
            $('#shipping-address').text(orderData.orderAddress);
            $('#payment-method').text(orderData.paymentMethod || '카드');
            $('#total-amount').text(orderData.totalAmount.toLocaleString() + '원');
          }
        }
      },
      error: function () {
        alert("주문 완료 정보를 불러오는 데 실패했습니다.");
      }
    });

    $('.view-order-link').click(function () {
      location.href = '/mypage/manage';
    });

    $('.home-link').click(function () {
      location.href = '/main';
    });
  });