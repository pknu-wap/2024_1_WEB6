var title = document.getElementsByClassName('web-title');
title[0].onclick = function () {
    window.location.href = '/main_page/main.html';
};



function showValidation() {
    let nickname = document.getElementById('nickname').value;
    let newPassword = document.getElementById('newPassword').value;
    if (nickname == "") {
        alert("공백 또는 입력하지 않은 부분이 있습니다.");
    }
    else if(newPassword == ""){
        alert("공백 또는 입력하지 않은 부분이 있습니다.");
    }
}


