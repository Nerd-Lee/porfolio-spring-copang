// 서버에서 전달된 에러메시지가 있는 경우 alert을 띄워주는 함수
function checkErrorMessage(msg){
	if(msg && msg !== 'null' && msg !== ''){
		alert(msg);
	}
}