document.addEventListener("DOMContentLoaded", function() {
    const productId = window.location.pathname.split("/").pop();
    const quantityInput = document.getElementById("quantity");
    const quantityBottomInput = document.getElementById("quantity-bottom");
    const priceElement = document.getElementById("price");
    const totalPriceElement = document.getElementById("total-price");
    const productPrice = parseInt(priceElement.innerText.replace(/\D/g, ""));

    const updatePrice = () => {
      const quantity = parseInt(quantityInput.value);
      const totalPrice = productPrice * quantity;
      priceElement.innerText = totalPrice.toLocaleString() + "원";
      totalPriceElement.innerText = "총 상품 금액 " + totalPrice.toLocaleString() + "원";
    };

    quantityInput.addEventListener("input", function() {
      quantityBottomInput.value = quantityInput.value;
      updatePrice();
    });

    quantityBottomInput.addEventListener("input", function() {
      quantityInput.value = quantityBottomInput.value;
      updatePrice();
    });


// 상단 장바구니 버튼 이벤트 리스너
const cartLink = document.getElementById("cart-link");
cartLink.addEventListener("click", function(event) {
  event.preventDefault();
  const quantity = parseInt(quantityInput.value);
  addToCart(productId, quantity);
});

// 하단 장바구니 버튼 이벤트 리스너
const cartLinkBottom = document.getElementById("cart-link-bottom");
cartLinkBottom.addEventListener("click", function(event) {
  event.preventDefault();
  const quantity = parseInt(quantityBottomInput.value);
  addToCart(productId, quantity);
});

// 장바구니 추가 AJAX 요청 함수
function addToCart(productId, quantity) {
  $.ajax({
    type: "POST",
    url: "/product/add",
    data: { productId: productId, quantity: quantity },
    success: function() {
      alert("장바구니에 추가되었습니다.");
      location.href = "/cart";
    },
    error: function (xhr) {
      if (xhr.status === 409) {
        updateCartQuantity(productId, quantity);
      } else {
        alert("장바구니에 추가하는 데 실패했습니다.");
      }
    }
  });
}

// 장바구니 수량 업데이트 함수 (동일 상품일 경우 수량만 증가)
function updateCartQuantity(productId, additionalQuantity) {
  $.ajax({
    type: "PUT",
    url: "/cart/update",
    data: { productId: productId, additionalQuantity: additionalQuantity },
    success: function () {
      alert("장바구니 수량이 업데이트되었습니다.");
      location.href = "/cart";
    },
    error: function () {
      alert("장바구니 수량 업데이트에 실패했습니다.");
    }
  });
}


// 상단 구매 버튼 이벤트 리스너
const buyLink = document.getElementById("buy-link");
buyLink.addEventListener("click", function(event) {
  event.preventDefault();
  const quantity = parseInt(quantityInput.value);
  buyNow(productId, quantity);
});

// 하단 구매 버튼 이벤트 리스너
const buyLinkBottom = document.getElementById("buy-link-bottom");
buyLinkBottom.addEventListener("click", function(event) {
  event.preventDefault();
  const quantity = parseInt(quantityBottomInput.value);
  buyNow(productId, quantity);
});



// 바로 구매 AJAX 요청 함수
function buyNow(productId, quantity) {
  $.ajax({
    type: "POST",
    url: "/product/buy-now",
    contentType: "application/json",
        data: JSON.stringify({ productId: productId, quantity: quantity }),
        success: function(response) {
          console.log("Buy Now - Product ID:", productId, "Quantity:", quantity);  
          // 주문 ID를 localStorage에 저장하고 주문 페이지로 이동
          localStorage.setItem("productId", productId);
          localStorage.setItem("quantity", quantity);
          location.href = "/order";

          // 주문 정보 사용 후 Local Storage에서 제거
          localStorage.removeItem("productId");
          localStorage.removeItem("quantity");
        },
        error: function() {
          alert("주문 페이지로 이동하는 데 실패했습니다.");
        }
      });
    }

    const tabs = document.querySelectorAll(".tab-section div");
    const contents = document.querySelectorAll(".tab-content");

    tabs.forEach((tab) => {
      tab.addEventListener("click", () => {
        tabs.forEach(t => t.classList.remove("active"));
        tab.classList.add("active");
        contents.forEach(c => c.classList.remove("active"));
        document.getElementById(tab.getAttribute("data-target")).classList.add("active");
      });
    });
  });