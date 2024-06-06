// 웹 페이지 이동
document.querySelector(".web-title").onclick = () => {
  window.location.href = "../main_page/index.html";
};

document.querySelector(".login-button").onclick = () => {
  window.location.href = "../login/login-page.html";
};

document.querySelector(".signup-button").onclick = () => {
  window.location.href = "../join_page/join_page2.html";
};

// 검색 결과 및 페이지네이션 설정
document.addEventListener("DOMContentLoaded", () => {
  const urlParams = new URLSearchParams(window.location.search);
  const query = urlParams.get("query") || "";
  const option = urlParams.get("option") || "title";
  const headingElement = document.getElementById("search-result-heading");
  const noResultsMessage = document.getElementById("no-result-message");

  if (query) {
    headingElement.innerHTML = `<span>"${query}"의 검색 결과</span>`;
    performSearch(option, query);
  } else {
    noResultsMessage.innerHTML = `<span>검색 결과가 없습니다. 다시 입력해주세요.</span>`;
  }

  document
    .getElementById("search-form")
    .addEventListener("submit", async (event) => {
      event.preventDefault();
      const query = document.getElementById("search-input").value;
      const option = document.getElementById("search-option").value;

      if (!query) {
        alert("검색어를 입력하세요.");
        return;
      } else {
        headingElement.innerHTML = `<span>"${query}"의 검색 결과</span>`;
        performSearch(option, query);
      }
    });

  async function performSearch(option, query) {
    try {
      if (["nation", "genre", "actor", "director"].includes(option)) {
        const data = await searchByTag(option, query);
        console.log("Data fetched by tag:", data);
        displayResults(data, 1);
        setupPagination(data);
      } else {
        const response = await searchMovies(option, query);
        const data = await response.json();

        if (response.ok && data.success && data.data.length > 0) {
          displayResults(data.data, 1);
          setupPagination(data.data);
        } else {
          noResultsMessage.innerHTML = `<span>검색 결과가 없습니다.</span>`;
        }
      }
    } catch (error) {
      console.error("Error fetching movie data:", error);
      noResultsMessage.innerHTML = `<span>검색 도중 오류가 발생했습니다.</span>`;
    }
  }

  async function searchByTag(option, query) {
    const response = await fetch(
      `https://port-0-web6-1pgyr2mlvnqjxex.sel5.cloudtype.app/movies/search/json?option=${option}&query=${encodeURIComponent(
        query
      )}`,
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
      }
    );
    const data = await response.json();
    // `data`가 배열이 아닌 경우 배열로 만들어 반환
    return Array.isArray(data.data) ? data.data : [data];
  }
});

async function searchMovies(option, query) {
  const response = await fetch(
    `https://port-0-web6-1pgyr2mlvnqjxex.sel5.cloudtype.app/movies/search/json?option=${option}&query=${encodeURIComponent(
      query
    )}`,
    {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    }
  );
  return response;
}

function displayResults(data, page) {
  const resultsContainer = document.getElementById("results-container");
  resultsContainer.innerHTML = "";

  const filteredData = data.filter(
    (item) => item.postersList && item.postersList.length > 0
  );

  const itemsPerPage = 10; // 한 페이지에 10개의 결과를 표시
  const startIndex = (page - 1) * itemsPerPage;
  const endIndex = startIndex + itemsPerPage;
  const pageData = filteredData.slice(startIndex, endIndex);

  pageData.forEach((item) => {
    const title = item.title || "제목 없음";
    const genre = item.genre || "장르 정보 없음";
    const nation = item.nation || "국가 정보 없음";
    const posterUrl =
      item.postersList && item.postersList.length > 0
        ? item.postersList[0]
        : null;

    if (posterUrl) {
      const itemElement = document.createElement("div");
      itemElement.classList.add("result-item");

      itemElement.innerHTML = `
                  <img src="${posterUrl}" alt="${title} 포스터">
                  <h3 class="result-title">${title}</h3>
                  <p class="result-details">
                      <span class="result-genre">${genre}</span>
                      <span class="result-nation">· ${nation}</span>
                  </p>
              `;
      itemElement.addEventListener("click", () => {
        window.location.href = `/detail_page/detail_page.html?id=${item.movieId}`;
      });
      resultsContainer.appendChild(itemElement);
    }
  });
}

function setupPagination(data) {
  const pagination = document.getElementById("pagination");

  // 포스터 이미지가 있는 데이터만 필터링 (수정된 부분)
  const filteredData = data.filter(
    (item) => item.postersList && item.postersList.length > 0
  );

  const itemsPerPage = 10; // 한 페이지에 10개의 결과를 표시 (수정된 부분)
  const totalPages = Math.ceil(filteredData.length / itemsPerPage);

  pagination.innerHTML = "";

  for (let i = 1; i <= totalPages; i++) {
    const button = document.createElement("button");
    button.textContent = i;
    button.addEventListener("click", () => {
      displayResults(filteredData, i); // 필터된 데이터를 사용 (수정된 부분)
    });
    pagination.appendChild(button);
  }
}