function login() {
    var id = document.querySelector("#id")
    var pw = document.querySelector("#pw")

    if(id.value == "" || pw.value == "") alert("로그인을 할 수 없습니다.");
    else location.href = "main.html"
}

function back() {   // 뒤로가기?
    history.go(-1);
    history.back();
}

function create_id() {
    var id = document.querySelector("#id");
    var pw = document.querySelector("#pw");

    

    
}

