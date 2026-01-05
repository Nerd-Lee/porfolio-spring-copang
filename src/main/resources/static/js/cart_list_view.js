// 페이지 로드 시에도 한 번 실행해서 초기값 0원 세팅
window.onload = function() {
    updateOrderSum();
};

// 전체 선택/해제 로직
function checkAll() {
    const isChecked = document.getElementById('checkAllBox').checked;
    const checkboxes = document.getElementsByName('cartItemIds');
    checkboxes.forEach((checkbox) => {
        checkbox.checked = isChecked;
    });
}

// 주문 버튼 클릭 시 실행될 검증 함수
function validateOrder() {
	const checkboxes = document.getElementsByName('cartItemIds');
	let isAnyChecked = false;

	// 하나라도 체크되어 있는지 확인
	for (let i = 0; i < checkboxes.length; i++) {
		if (checkboxes[i].checked) {
			isAnyChecked = true;
			break;
		}
	}
	
	if (!isAnyChecked) {
		alert("주문할 상품을 선택해주세요.");
		return false; // form 전송 중단
	}
	return true; // form 전송 진행
}

function updateOrderSum() {
	const checkBoxes = document.getElementsByName("cartItemIds");
	const displayTarget = document.getElementById('finalTotalPrice');
	let total = 0;
	
	checkBoxes.forEach((cekbox) => {
		if(cekbox.checked){
			// 체크박스가 포함된 행의 가격 데이터를 가져온다.
			const row = cekbox.closest('tr');;
			const price = parseInt(row.querySelector('.unitPrice').getAttribute('data-price'));
			total += price;	
		}
	});
	
	displayTarget.innerText = total.toLocaleString() + '원';
}