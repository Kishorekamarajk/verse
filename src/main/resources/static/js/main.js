(function () {
    var navbar = document.querySelector('.site-navbar');
    if (!navbar) return;

    var SCROLL_THRESHOLD = 60;

    function updateNavbar() {
        if (window.scrollY > SCROLL_THRESHOLD) {
            navbar.classList.add('scrolled');
        } else {
            navbar.classList.remove('scrolled');
        }
    }

    document.addEventListener('DOMContentLoaded', updateNavbar);
    window.addEventListener('scroll', updateNavbar, { passive: true });
})();

(function () {
    function initNavbarDropdownAutoClose() {
        var dropdowns = document.querySelectorAll('.site-navbar .nav-item.dropdown');
        if (!dropdowns.length) return;

        var desktopQuery = window.matchMedia('(min-width: 992px)');

        function closeDropdown(dropdown) {
            var toggle = dropdown.querySelector('.dropdown-toggle');
            var menu = dropdown.querySelector('.dropdown-menu');
            if (!toggle || !menu) return;

            if (window.bootstrap && window.bootstrap.Dropdown) {
                window.bootstrap.Dropdown.getOrCreateInstance(toggle).hide();
            }

            dropdown.classList.remove('show');
            menu.classList.remove('show');
            toggle.classList.remove('show');
            toggle.setAttribute('aria-expanded', 'false');
        }

        function closeOpenDropdownsExcept(activeDropdown) {
            if (!desktopQuery.matches) return;

            dropdowns.forEach(function (dropdown) {
                if (dropdown === activeDropdown) return;
                closeDropdown(dropdown);
            });
        }

        dropdowns.forEach(function (dropdown) {
            var toggle = dropdown.querySelector('.dropdown-toggle');

            dropdown.addEventListener('mouseleave', function () {
                if (!desktopQuery.matches) return;
                closeDropdown(dropdown);
            });

            dropdown.addEventListener('focusout', function () {
                window.setTimeout(function () {
                    if (dropdown.contains(document.activeElement)) return;
                    closeDropdown(dropdown);
                }, 0);
            });

            if (toggle) {
                toggle.addEventListener('click', function () {
                    closeOpenDropdownsExcept(dropdown);
                });
            }
        });

        document.addEventListener('pointermove', function (event) {
            if (!desktopQuery.matches) return;

            var target = event.target;
            if (!target || typeof target.closest !== 'function') return;

            var activeDropdown = target.closest('.site-navbar .nav-item.dropdown');
            if (activeDropdown) {
                closeOpenDropdownsExcept(activeDropdown);
                return;
            }

            dropdowns.forEach(function (dropdown) {
                closeDropdown(dropdown);
            });
        });
    }

    document.addEventListener('DOMContentLoaded', initNavbarDropdownAutoClose);
})();

(function () {
    function initScrollReveal() {
        var items = document.querySelectorAll('[data-reveal]');
        if (!items.length) return;

        var reduceMotion = window.matchMedia && window.matchMedia('(prefers-reduced-motion: reduce)').matches;
        if (reduceMotion || !('IntersectionObserver' in window)) {
            items.forEach(function (el) { el.classList.add('reveal-visible'); });
            return;
        }

        var observer = new IntersectionObserver(function (entries, obs) {
            entries.forEach(function (entry) {
                if (!entry.isIntersecting) return;
                var el = entry.target;
                var delay = el.getAttribute('data-reveal-delay');
                if (delay) {
                    el.style.transitionDelay = delay + 'ms';
                }
                el.classList.add('reveal-visible');
                obs.unobserve(el);
            });
        }, { threshold: 0.15, rootMargin: '0px 0px -40px 0px' });

        items.forEach(function (el) { observer.observe(el); });
    }

    document.addEventListener('DOMContentLoaded', initScrollReveal);
})();

(function () {
    function initSpeakerMoreButton() {
        var button = document.querySelector('[data-speakers-more-button]');
        var moreSpeakers = document.querySelector('[data-more-speakers]');
        if (!button || !moreSpeakers) return;

        button.addEventListener('click', function () {
            moreSpeakers.classList.remove('hidden');
            moreSpeakers.classList.add('grid');
            button.parentElement.classList.add('hidden');

            moreSpeakers.querySelectorAll('[data-reveal]').forEach(function (el) {
                el.classList.add('reveal-visible');
            });
        });
    }

    document.addEventListener('DOMContentLoaded', initSpeakerMoreButton);
})();
