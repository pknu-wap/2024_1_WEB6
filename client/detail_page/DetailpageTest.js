document.addEventListener("DOMContentLoaded", function () {
    const paramValue = getQueryParam('paramname');

    fetch('https://port-0-web6-1pgyr2mlvnqjxex.sel5.cloudtype.app/movies/detail/'+paramValue, {
        credentials: 'include'
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok ' + response.statusText);
            }
            return response.json(); // JSON 응답을 JavaScript 객체로 변환
        })
        .then(data => {
            // document.getElementById("response").innerHTML = data.hasNext;
            var title = document.getElementById("title");
            title.innerHTML += data[0].title;
            
            var prodYear = document.getElementById("year");
            prodYear.innerHTML += data[0].prodYear + "년";

            var actors = document.getElementById("actors");
            // 배우 목록 추가 (예외 처리 포함)
            var actorList = data[0].actors.actor;
            if(actorList[0].actorNm == ""){  actors.innerHTML += "배우 없음"
            } else if (actorList.length === 1) {
                actors.innerHTML += actorList[0].actorNm;
            } else if (actorList.length === 2) {
                actors.innerHTML += actorList[0].actorNm + ", " + actorList[1].actorNm;
            } else if (actorList.length >= 3) {
                actors.innerHTML += actorList[0].actorNm + ", " + actorList[1].actorNm + ", " + actorList[2].actorNm;
            }
            
            var nation = document.getElementById("country");
            nation.innerHTML += data[0].nation;

            var plotText = document.getElementById("summary");
            plotText.innerHTML += data[0].plotText;

            var runtime = document.getElementById("runtime");
            runtime.innerHTML += data[0].runtime + "분";


            var rating = document.getElementById("rating");
            if(data[0].rating == "") {
                rating.innerHTML += "전체관람가";}
            else {
                rating.innerHTML += data[0].rating;
            }
            

            var genre = document.getElementById("genre");
            if(data[0].genre == "") {
                genre.innerHTML += "복합";}
            else {
                genre.innerHTML += data[0].genre;
            }
            // console.log(title);
        })
        .catch(error => {
            console.error('There was a problem with the fetch operation:', error);
        });
});