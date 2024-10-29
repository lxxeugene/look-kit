$(document).ready(function () {
    // 좋아요 추가 및 삭제 클릭이벤트
    $(".like-btn").click( function (){
        let $this = $(this);
        let productItem= $(this).closest('.product-item');
        let productID = parseInt(productItem.find('.hidden-id').text().trim());  // 숫자타입으로 변환
        console.log("productID",productID);

        fetch(`/wishlist/item?productId=${productID}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            },
        })
            .then(response => {
            if (!response.ok) {
                throw new Error('응답에러 ' + response.statusText);
            }
            return response.json();
        })
            .then(result => {
            console.log('결과 : ', result);
            if ( result.message == '상품추가') {
                //성공
                $this.attr("src", "/images/icon/heart1.svg");
            }else if(result.message == '상품삭제')
            //삭제
            $this.attr("src", "/images/icon/heart2.svg");
            else {
                //실패
                console.log("상품 찜하기 추가&삭제  실패");
            }
        })
            .catch(error => {
            console.error('위시리스트 추가 중 에러 발생 : ', error);
        });
    })

    // 찜한 상품 확인
    $('.hidden-id').each(function() {
        var $this = $(this); // 현재 요소를 $this에 저장
        var id = parseFloat($(this).text()); // 현재 요소의 텍스트를 숫자로 변환하여 반환
        console.log("id",id);
        // Fetch API 사용
        fetch('/wishlist/check?itemId=' + id, {
            method: 'GET'
        })
            .then(response => {
            if (!response.ok) {
                throw new Error('응답에러 ' + response.statusText);
            }
            return response.json();
        })
            .then(result => {
            console.log('결과 : ', result);
            if ( result.message == 'addedItem') {
                console.log("추가된상품");
                var $heart = $this.closest('.product-item').find('.like-btn');
                $heart.attr("src", "/images/icon/heart1.svg");
            }else {
                return;
            }
        })
            .catch(error => {
            console.error('위시리스트 상품 확인 중 에러 발생 : ', error);
        });
    });
})
