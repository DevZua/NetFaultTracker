const router = {
    routes: {
        '/': { title: 'Home', file: 'index.html' },
        '/index.html': { title: 'Home', file: 'index.html' },
        '/list': { title: '이슈 목록', file: 'list.html' },
        '/list.html': { title: '이슈 목록', file: 'list.html' },
        '/form': { title: '새 이슈 등록', file: 'form.html' },
        '/form.html': { title: '새 이슈 등록', file: 'form.html' },
        '/view': { title: '이슈 상세 보기', file: 'view.html' },
        '/view.html': { title: '이슈 상세 보기', file: 'view.html' },
        '/search': { title: '검색 결과', file: 'search-results.html' },
        '/search-results.html': { title: '검색 결과', file: 'search-results.html' }
    },
    navigate: function(path) {
        console.log('Navigating to:', path);
        const route = this.routes[path] || this.routes['/'];
        document.title = `${route.title} - NetFaultTracker`;
        fetch(route.file)
            .then(response => {
                if (!response.ok) {
                    throw new Error('HTTP error ' + response.status);
                }
                return response.text();
            })
            .then(html => {
                console.log('Loaded HTML:', html.substring(0, 100) + '...');
                document.getElementById('content').innerHTML = html;
                history.pushState(null, '', path);
            })
            .catch(error => {
                console.error('Error loading page:', error);
                document.getElementById('content').innerHTML = '<p>Error loading page. Please try again.</p>';
            });
    }
};

document.addEventListener('DOMContentLoaded', function() {
    document.body.addEventListener('click', function(e) {
        if (e.target.tagName === 'A') {
            e.preventDefault();
            const href = e.target.getAttribute('href');
            console.log('Clicked link:', href);
            router.navigate(href);
        }
    });

    window.addEventListener('popstate', function() {
        router.navigate(window.location.pathname);
    });

    // 초기 페이지 로드
    router.navigate(window.location.pathname);
});