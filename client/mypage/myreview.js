
// 프로필 수정 페이지로 이동
function editProfile() {
    var editProfileUrl = "../mypage/mypage.html";
    window.open(editProfileUrl, "_blank");
}

// 닉네임을 URL 인코딩하는 함수
function encodeNickname(nickname) {
    return encodeURIComponent(nickname);
}

// 사용자 닉네임에 기반한 리뷰 데이터를 가져오는 함수
async function fetchReviews(nickname) {
    var encodedNickname = encodeNickname(nickname);
    var apiUrl = `https://port-0-web6-1pgyr2mlvnqjxex.sel5.cloudtype.app/api/writer/${encodedNickname}/reviews`;

    try {
        const response = await fetch(`https://port-0-web6-1pgyr2mlvnqjxex.sel5.cloudtype.app/api/writer/${encodedNickname}/reviews`, {
             method: 'GET',
             credentials: 'include',
             headers: {
                'Content-Type': 'application/json'
            },
        });

        if (!response.ok) {
            throw new Error('Network response was not ok ' + response.statusText);
        }
        
        const data = await response.json();
        displayReviews(data);

    } catch (error) {
        console.error('Error fetching reviews:', error);
    }
}

// 리뷰 데이터를 화면에 표시하는 함수
function displayReviews(response) {
    var reviewList = document.getElementById("review-list");
    reviewList.innerHTML = ''; // 기존 내용을 지움

    if (response.data.length === 0) {
        reviewList.innerHTML = '<p class="no-reviews">작성한 리뷰가 없습니다! 첫 리뷰를 작성해보세요!</p>';
        return;
    }

    response.data.forEach(article => {
        var reviewItem = document.createElement("div");
        reviewItem.className = "review-item";

        var reviewTitle = document.createElement("div");
        reviewTitle.className = "review-title";
        var titleLink = document.createElement("a");
        //titleLink.href = `movie_detail_page.html?movie=${encodeURIComponent(review.movieTitle)}`;
        titleLink.textContent = article.title;
        reviewTitle.appendChild(titleLink);

        var reviewDetails = document.createElement("div");
        reviewDetails.className = "review-details";
        reviewDetails.innerHTML = `
            <p>총 별점: ${article.grade}</p>
            <p>작성자: ${article.review.nickname}</p>
            <p>작성일시: ${article.review.createDate}</p>
            <p>수정여부: ${article.review.edit ? '수정됨' : ''}</p>
            <p>스포일러 여부: ${article.review.spoiler ? '있음' : '없음'}</p>
            <p>대댓글 개수: ${article.review.commentsCount}</p>
        `;

        var reviewContent = document.createElement("div");
        reviewContent.className = "review-content";
        reviewContent.textContent = article.review.content;
        
        var reviewRating = document.createElement("div");
        reviewRating.className = "review-rating";
        reviewRating.textContent = `리뷰 별점: ★${article.review.grade}`;

        reviewItem.appendChild(reviewTitle);
        reviewItem.appendChild(reviewDetails);
        reviewItem.appendChild(reviewContent);
        reviewItem.appendChild(reviewRating);

        reviewList.appendChild(reviewItem);
    });
}

// 로그인 상태 확인 및 리뷰 데이터 가져오기
document.addEventListener('DOMContentLoaded', async () => {
    try {
        const response = await fetch('https://port-0-web6-1pgyr2mlvnqjxex.sel5.cloudtype.app/api/mypage', {
            method: 'GET',
            credentials: 'include' // 쿠키를 포함하여 요청
        });

        const data = await response.json();
        console.log('Response Data:', data);

        if(response.ok) {
            if(data.success){
                let nickname = data.data.nickname;
                document.querySelector(".username").textContent = nickname;
                document.querySelector(".user-email").textContent = data.data.loginId;
                fetchReviews(nickname);
            }
            else {
                console.log('정보를 로드하는 데 실패했습니다.');
            }   
        } else {
            console.log('정보를 로드하는 데 실패했습니다.');
        }
    } catch (errors) {
        console.error('서버 에러:', errors);
    }
});