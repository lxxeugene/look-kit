function confirmPurchase(button, orderId, productId) {
    Swal.fire({
        title: '구매확정 하시겠습니까?',
        html: '<p class="custom-warning">구매확정 시 교환/반품을 할 수 없습니다</p>',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#1a202c',  // 확인 버튼 색상
        cancelButtonColor: '#ffffff',  // 취소 버튼 배경색 (하얀색)
        confirmButtonText: '구매확정',
        cancelButtonText: '취소',
        customClass: {
            title: 'custom-title',  // 제목 스타일 커스터마이즈
            popup: 'custom-popup',  // 모달 팝업 전체 스타일 커스터마이즈
            confirmButton: 'custom-confirm-button',  // 확인 버튼 스타일
            cancelButton: 'custom-cancel-button',  // 취소 버튼 스타일
            htmlContainer: 'custom-html',  // 텍스트 스타일 커스터마이즈
        }
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                url: '/mypage/manage/update/confirmed',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify({ orderId : orderId, productId : productId }),
                success: function(response) {
                    if (response.success) {
                        const parentDiv = button.closest(".actions");
                        button.style.display = "none";

                        const confirmedText = document.createElement("span");
                        confirmedText.textContent = "구매확정";
                        confirmedText.classList.add("confirmed-text");
                        parentDiv.appendChild(confirmedText);

                        Swal.fire({
                            title: '구매확정이 완료되었습니다',
                            html: '<p>감사합니다 :)</p>',
                            icon: 'success',
                            confirmButtonColor: '#1a202c',
                            confirmButtonText: '확인',
                            customClass: {
                                title: 'custom-title',
                                popup: 'custom-popup',
                                confirmButton: 'custom-confirm-button',
                                htmlContainer: 'custom-html'
                            }
                        });
                    } else {
                        Swal.fire({
                            title: '오류 발생',
                            text: '구매확정 업데이트에 실패했습니다. 다시 시도해 주세요.',
                            icon: 'error',
                            confirmButtonColor: '#d33',
                            confirmButtonText: '확인'
                        });
                    }
                },
                error: function() {
                    Swal.fire({
                        title: '오류 발생',
                        text: '서버와 통신 중 오류가 발생했습니다. 다시 시도해 주세요.',
                        icon: 'error',
                        confirmButtonColor: '#d33',
                        confirmButtonText: '확인'
                    });
                }
            });
        }
    });
}