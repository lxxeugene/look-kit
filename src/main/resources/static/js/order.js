$(document).ready(function () {
    const productId = localStorage.getItem("productId");
    const quantity = localStorage.getItem("quantity");

    if (productId && quantity) {
        // Product 페이지에서 바로 구매하기 버튼을 클릭한 경우
        loadProductOrder(productId, quantity);
    } else {
        // Cart 페이지에서 구매하기 버튼을 클릭한 경우
        loadCartOrder();
    }

    // 상품 정보를 로드하여 주문 페이지에 렌더링하는 함수 (Product 페이지에서 바로 구매 시나리오)
    function loadProductOrder(productId, quantity) {
        $.ajax({
            type: "GET",
            url: "/product/" + productId,
            success: function (product) {
                if (!product || !product.productId) {
                    alert("상품 정보를 불러오는 데 실패했습니다.");
                    return;
                }

                const orderItem = {
                    productId: product.productId,
                    productName: product.productName,
                    brandName: product.brandName,
                    quantity: parseInt(quantity),
                    productPrice: product.productPrice
                };
                renderOrderItems([orderItem]);

                // LocalStorage 정보 삭제 (주문이 제대로 로드된 후 삭제)
                localStorage.removeItem("productId");
                localStorage.removeItem("quantity");
            },
            error: function () {
                alert("상품 정보를 불러오는 데 실패했습니다.");
            }
        });
    }

    // 장바구니 정보를 로드하여 주문 페이지에 렌더링하는 함수 (Cart 페이지에서 구매 시나리오)
    function loadCartOrder() {
        $.ajax({
            type: "GET",
            url: "/cart/items",
            success: function (cartItems) {
                if (cartItems && cartItems.length > 0) {
                    renderOrderItems(cartItems);
                } else {
                    alert("주문할 상품이 없습니다. 장바구니를 확인해주세요.");
                    location.href = "/cart";
                }
            },
            error: function () {
                alert("장바구니 정보를 불러오는 데 실패했습니다.");
            }
        });
    }

    // 주문 항목을 렌더링하는 함수
    function renderOrderItems(items) {
        $("#product-list").empty(); // 이전에 추가된 항목들을 제거하고 새로 추가

        items.forEach(item => {
            if (!item.productId || !item.productName || !item.productPrice) {
                console.warn("잘못된 데이터:", item);
                return;
            }

            $("#product-list").append(`
                <div class="order-item" data-order-id="${item.orderId}">
                    <div class="product-item">
                        <div class="item-thumbnail">
                            <a href="/product/${item.productId}">
                                <img src="/images/products/0${item.productId}/${item.productId}_thumbnail.webp" alt="상품 썸네일" />
                            </a>
                        </div>
                        <div class="item-description">
                            <div class="brand-name2">${item.brandName}</div>
                            <div class="product-name">${item.productName}</div>
                            <div class="product-variant">${item.quantity}개</div>
                        </div>
                        <div class="item-price">
                            <strong>${item.productPrice.toLocaleString()}원</strong>
                        </div>
                    </div>
                </div>
            `);
        });

        // 총 가격과 총 수량을 계산하여 화면에 표시
        let totalPrice = items.reduce((sum, item) => sum + (item.productPrice * item.quantity), 0);
        let totalQuantity = items.reduce((sum, item) => sum + item.quantity, 0);
        $(".summary-value").text(totalPrice.toLocaleString() + "원");
        $("#pay-button").text(`총 ${totalQuantity}개 / ${totalPrice.toLocaleString()}원 결제하기`);
    }

    // 결제 버튼 클릭 이벤트
  $("#pay-button").click(function () {
    let orderName = $("#product-list .product-item:first .product-name").text();
    let buyerName = $("#buyer-recipient-name").val();
    let buyerTel = $("#buyer-phone-number").val();
    let buyerAddr = $("#buyer-address").val();
    let totalAmount = parseInt($(".summary-value").text().replace(/\D/g, ""));

    if (!$("input[name='payment-method']:checked").val()) {
      alert("결제 수단을 선택해주세요.");
      return;
    }

    if (!$("input[name='agreement1']").is(":checked") ||
        !$("input[name='agreement2']").is(":checked") ||
        !$("input[name='agreement3']").is(":checked") ||
        !$("input[name='final-agreement']").is(":checked")) {
      alert("모든 필수 동의 항목에 체크해주세요.");
      return;
    }

    IMP.init("imp40354073");
  
    IMP.request_pay({
        pg: 'kcp',
        pay_method: 'card',
        merchant_uid: 'merchant_' + new Date().getTime(),
        name: orderName,
        amount: totalAmount,
        buyer_name: buyerName,
        buyer_tel: buyerTel,
        buyer_addr: buyerAddr
    }, function (rsp) {
        if (rsp.success) {
            alert("결제가 완료되었습니다.\n고유ID: " + rsp.imp_uid);

            
            const paymentData = {
              userId: userId, 
              orderName: orderName,
              totalAmount: totalAmount,
              orderAddress: buyerAddr,
              orderAddressee: buyerName,
              orderPhone: buyerTel
        
            };
            
            $.ajax({
              type: "POST",
              url: "/order/orderComplete",
              contentType: "application/json",
              data: JSON.stringify(paymentData),
              success: function() {
                location.href = "/order/orderComplete";
              },
              error: function() {
                alert("결제를 완료하는 데 실패했습니다.");
              }
            });
        } else {
            alert("결제에 실패했습니다. 실패사유: " + rsp.error_msg);
        }
    });
  });


    // 주소 등록 버튼 클릭 이벤트
    $("#add-address-button").click(function () {
        openAddressRegistration();
    });

    // 주소 등록 팝업 열기
    function openAddressRegistration() {
        window.open('/order/addAddress', '주소 등록', 'width=500,height=700');
        window.addEventListener("message", function (event) {
            if (event.data.type === "address") {
                const addressData = event.data.address;
                $("#buyer-address-name").val(addressData.addressName);
                $("#buyer-recipient-name").val(addressData.recipientName);
                $("#buyer-phone-number").val(addressData.phoneNumber);
                $("#buyer-address").val(addressData.fullAddress);
            }
        }, false);
    }
});