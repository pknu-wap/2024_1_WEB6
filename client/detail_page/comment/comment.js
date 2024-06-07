document.querySelector(".web-title").onclick = () => {
    window.location.href = "../../main_page/index.html";
};

document.addEventListener('DOMContentLoaded', async () => {
    const urlParams = new URLSearchParams(window.location.search);
    const reviewId = urlParams.get('review-id');
    if (reviewId) {
        let userNickname = await fetchUserNickname();
        fetchComments(reviewId, userNickname);
    } else {
        console.error('리뷰 ID를 찾을 수 없습니다.');
    }
});

// 닉네임에서 "님"을 제거하는 함수
function removeSuffix(nickname) {
    if (nickname.endsWith("님")) {
        return nickname.slice(0, -1); // 끝에서 1글자("님")를 제거
    }
    return nickname;
}

// 현재 페이지에 접속한 유저의 닉네임을 가져오는 함수
async function fetchUserNickname() {
    var apiUrl = 'https://port-0-web6-1pgyr2mlvnqjxex.sel5.cloudtype.app/api/main';

    try {
        const response = await fetch(apiUrl, {
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
        console.log(data);

        let cleanedNickname = removeSuffix(data.message); // nickname 변수 대신 data.message 사용
        console.log(cleanedNickname);

        return cleanedNickname;
        
    } catch (error) {
        console.error('Error fetching user nickname:', error);
        return null;
    }
}

// 대댓글 데이터를 가져오는 함수
async function fetchComments(reviewId, userNickname) {
    var apiUrl = `https://port-0-web6-1pgyr2mlvnqjxex.sel5.cloudtype.app/api/reviews/${reviewId}/comments`;

    try {
        const response = await fetch(apiUrl, {
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
        displayComments(data, userNickname, reviewId);

    } catch (error) {
        console.error('Error fetching comments:', error);
    }
}

// 대댓글 데이터를 화면에 표시하는 함수
function displayComments(response, userNickname, reviewId) {
    var commentsContainer = document.getElementById("comments-container");
    commentsContainer.innerHTML = ''; // 기존 내용을 지움

    if (response.data.length === 0) {
        commentsContainer.innerHTML = '<p class="no-comments">대댓글이 없습니다!</p>';
        return;
    }

    response.data.forEach(comment => {
        var commentItem = document.createElement("div");
        commentItem.className = "comment-item";
        
        var commentNickname = document.createElement("p");
        commentNickname.className = "comment-nickname";
        commentNickname.textContent = comment.nickname;

        var commentContent = document.createElement("p");
        commentContent.className = "comment-content";
        commentContent.textContent = comment.content;

        var commentDate = document.createElement("p");
        commentDate.className = "comment-date";
        commentDate.textContent = `작성일시: ${new Date(comment.createDate).toLocaleString()}`;

        commentItem.appendChild(commentNickname);
        commentItem.appendChild(commentContent);
        commentItem.appendChild(commentDate);

        // 현재 유저와 대댓글 작성자가 동일하면 삭제 버튼 추가
        if (userNickname === comment.nickname) {
            var deleteButton = document.createElement("button");
            deleteButton.className = "comment-delete";
            deleteButton.textContent = "삭제";
            deleteButton.setAttribute('data-comment-id', comment.id);
            deleteButton.setAttribute('data-review-id', reviewId);
            commentItem.appendChild(deleteButton);
        }

        commentsContainer.appendChild(commentItem);
    });

    // 삭제 버튼 클릭 이벤트 설정
    document.querySelectorAll('.comment-delete').forEach(element => {
        element.addEventListener('click', async function () {
            var commentId = this.getAttribute('data-comment-id');
            var reviewId = this.getAttribute('data-review-id');
            
            try {
                const response = await fetch(`https://port-0-web6-1pgyr2mlvnqjxex.sel5.cloudtype.app/api/reviews/${reviewId}/commentDelete/${commentId}`, {
                    method: 'DELETE',
                    credentials: 'include',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                });

                const data = await response.json();
                console.log('Response Data:', data);

                if(response.ok && data.success) {
                    alert('대댓글이 성공적으로 삭제되었습니다.');
                    location.reload(); // 페이지 새로고침
                } else {
                    alert('대댓글 삭제에 실패했습니다.');
                }
            } catch (errors) {
                console.error('서버 에러:', errors);
                alert('서버에 연결할 수 없습니다.');
            }
        });
    });
}
