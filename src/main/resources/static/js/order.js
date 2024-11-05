$(document).ready(function () {
    const productId = localStorage.getItem("productId");
    const quantity = localStorage.getItem("quantity");

    if (productId && quantity) {
        loadProductOrder(productId, quantity);
    } else {
        loadCartOrder();
    }

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

                localStorage.removeItem("productId");
                localStorage.removeItem("quantity");
            },
            error: function () {
                alert("상품 정보를 불러오는 데 실패했습니다.");
            }
        });
    }

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

    function renderOrderItems(items) {
        $("#product-list").empty();
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

                // 주문 상세 정보 수집
                let orderDetails = [];
                $("#product-list .order-item").each(function () {
                    let productId = $(this).find(".product-item a").attr("href").split('/').pop();
                    let quantity = parseInt($(this).find(".product-variant").text().replace(/\D/g, ''));
                    let productPrice = parseInt($(this).find(".item-price strong").text().replace(/\D/g, ''));

                    orderDetails.push({
                        productId: productId,
                        quantity: quantity,
                        productPrice: productPrice
                    });
                });

                const paymentData = {
                    totalAmount: totalAmount,
                    orderAddress: buyerAddr,
                    orderAddressee: buyerName,
                    orderPhone: buyerTel,
                    orderDetails: orderDetails
                };
                // const paymentData = {
                //     orderName: orderName,
                //     totalAmount: totalAmount,
                //     orderAddress: buyerAddr,
                //     orderAddressee: buyerName,
                //     orderPhone: buyerTel
                // };

                $.ajax({
                    type: "POST",
                    url: "/order/complete",
                    contentType: "application/json",
                    data: JSON.stringify(paymentData),
                    success: function (response) {
                        localStorage.setItem("latestOrderId", response.orderId);
                        location.href = "/order/orderComplete?orderId=" + response.orderId;
                    },
                    error: function () {
                        alert("결제를 완료하는 데 실패했습니다.");
                    }
                });
            } else {
                alert("결제에 실패했습니다. 실패사유: " + rsp.error_msg);
            }
        });
    });

    $("#add-address-button").click(function () {
        openAddressRegistration();
    });

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
