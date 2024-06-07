document.querySelector(".web-title").onclick = () => {
    window.location.href = "../main_page/index.html";
  };

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

    document.getElementById("count_review").textContent = response.data.length;

    if (response.data.length === 0) {
        reviewList.innerHTML = '<p class="no-reviews">작성한 리뷰가 없습니다! 첫 리뷰를 작성해보세요!</p>';
        return;
    }

    response.data.forEach(article => {
        var reviewItem = document.createElement("div");
        reviewItem.className = "review-item";

        var movieContainer = document.createElement("div");
        movieContainer.className = "movie-container";
        
        var reviewTitle = document.createElement("a");
        reviewTitle.className = "review-title";
        reviewTitle.textContent = article.title;
        reviewTitle.href = `../detail_page/detailpage.html?movieId=${article.movieId}&movieSeq=${article.movieSeq}`;
        var reviewRating = document.createElement("div");
        reviewRating.className = "review-rating";
        reviewRating.textContent = `★${article.grade}`;
        
        movieContainer.appendChild(reviewTitle);
        movieContainer.appendChild(reviewRating);

        var reviewDetails = document.createElement("div");
        reviewDetails.className = "review-details";
        reviewDetails.innerHTML = `
            <p style="color: black;">작성자: ${article.review.nickname}</p>
            <p>★ ${article.review.grade}</p>
            ${article.review.edit ? '<p class="additional-information">수정됨</p>' : ''}
            ${article.review.spoiler ? '<p class="additional-information">스포주의</p>' : ''}
            <p class="comment-count" data-review-id="${article.review.id}" style="cursor:pointer; color:black;">대댓글 ${article.review.commentsCount}</p> <!-- 대댓글 클릭 이벤트 추가 -->
             <p class="review-delete" data-review-id="${article.review.id}" style="cursor:pointer; color:black;">삭제</p> <!-- 삭제 클릭 이벤트 추가 -->
            `;

        var reviewDate = document.createElement("p");
        reviewDate.className = "review-date";
        reviewDate.textContent = `작성일시: ${new Date(article.review.createDate).toLocaleString()}`;

        var reviewContentContainer = document.createElement("div");
        reviewContentContainer.className = "review-content-container";
        

        var reviewContent = document.createElement("div");
        reviewContent.className = "review-content";
        reviewContent.textContent = article.review.content;

        reviewContentContainer.appendChild(reviewContent);
        reviewContentContainer.appendChild(reviewDetails.querySelector('.comment-count')); // 대댓글 개수를 review-content-container로 이동
        reviewContentContainer.appendChild(reviewDetails.querySelector('.review-delete'));

        reviewItem.appendChild(movieContainer);
        reviewItem.appendChild(reviewDate);
        reviewItem.appendChild(reviewDetails);
        reviewItem.appendChild(reviewContentContainer);

        reviewList.appendChild(reviewItem);
    });

document.querySelectorAll('.comment-count').forEach(element => {
    element.addEventListener('click', function () {
        var reviewId = this.getAttribute('data-review-id');
        window.location.href = `../detail_page/comment/comment.html?review-id=${reviewId}`;
    });    
});

    // 삭제 클릭 이벤트 설정
    document.querySelectorAll('.review-delete').forEach(element => {
        element.addEventListener('click', async function () {
            var reviewId = this.getAttribute('data-review-id');
            
            try {
                const response = await fetch(`https://port-0-web6-1pgyr2mlvnqjxex.sel5.cloudtype.app/api/reviewDelete/${reviewId}`, {
                    method: 'DELETE',
                    credentials: 'include' // 쿠키를 포함하여 요청
                });
        
                const data = await response.json();
                console.log('Response Data:', data);
        
                if(response.ok && data.success) {
                    alert('리뷰가 성공적으로 삭제되었습니다.');
                    location.reload(); // 페이지 새로고침
                } else {
                    alert('리뷰 삭제에 실패했습니다.');
                }
            } catch (errors) {
                console.error('서버 에러:', errors);
                alert('서버에 연결할 수 없습니다.');
            }
            
        });    
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