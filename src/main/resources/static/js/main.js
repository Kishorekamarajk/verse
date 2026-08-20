(function () {
    var SUPPORTED_LANGUAGES = ['en', 'ta', 'hi'];
    var GOOGLE_TRANSLATE_COOKIE = 'googtrans';
    var GOOGLE_TRANSLATE_ELEMENT_ID = 'google_translate_element';

    function currentLanguage() {
        var queryLang = new URLSearchParams(window.location.search).get('lang');
        if (SUPPORTED_LANGUAGES.indexOf(queryLang) >= 0) return queryLang;

        var googleLang = googleTranslateCookieLanguage();
        if (googleLang) return googleLang;

        var lang = document.documentElement.getAttribute('lang') || 'en';
        return SUPPORTED_LANGUAGES.indexOf(lang) >= 0 ? lang : 'en';
    }

    function googleTranslateCookieLanguage() {
        var match = document.cookie.match(new RegExp('(?:^|; )' + GOOGLE_TRANSLATE_COOKIE + '=([^;]*)'));
        if (!match) return null;

        var parts = decodeURIComponent(match[1]).split('/');
        var lang = parts[2];
        return SUPPORTED_LANGUAGES.indexOf(lang) >= 0 ? lang : null;
    }

    function updateButtons(lang) {
        document.querySelectorAll('[data-language-button]').forEach(function (button) {
            var isActive = button.getAttribute('data-language-button') === lang;
            button.classList.toggle('active', isActive);
            button.setAttribute('aria-pressed', isActive ? 'true' : 'false');
        });
    }

    function hideGoogleTranslateChrome() {
        document.documentElement.style.top = '0';
        document.body.style.top = '0';
        document.body.style.marginTop = '0';

        document.querySelectorAll('.goog-te-banner-frame, iframe.goog-te-banner-frame, body > .skiptranslate, #goog-gt-tt, .goog-te-balloon-frame').forEach(function (element) {
            element.style.display = 'none';
            element.style.height = '0';
            element.style.visibility = 'hidden';
        });
    }

    function changeLanguage(lang) {
        if (SUPPORTED_LANGUAGES.indexOf(lang) < 0) return;
        setGoogleTranslateLanguage(lang);
        var url = new URL(window.location.href);
        url.searchParams.set('lang', lang);
        window.location.href = url.toString();
    }

    function setCookie(name, value) {
        var cookie = name + '=' + value + ';path=/;max-age=31536000;SameSite=Lax';
        document.cookie = cookie;

        if (window.location.hostname.indexOf('.') > -1) {
            document.cookie = cookie + ';domain=.' + window.location.hostname;
        }
    }

    function deleteCookie(name) {
        document.cookie = name + '=;expires=Thu, 01 Jan 1970 00:00:00 GMT;path=/';

        if (window.location.hostname.indexOf('.') > -1) {
            document.cookie = name + '=;expires=Thu, 01 Jan 1970 00:00:00 GMT;path=/;domain=.' + window.location.hostname;
        }
    }

    function setGoogleTranslateLanguage(lang) {
        if (lang === 'en') {
            deleteCookie(GOOGLE_TRANSLATE_COOKIE);
            return;
        }

        setCookie(GOOGLE_TRANSLATE_COOKIE, '/en/' + lang);
    }

    function ensureGoogleTranslateElement() {
        if (document.getElementById(GOOGLE_TRANSLATE_ELEMENT_ID)) return;

        var element = document.createElement('div');
        element.id = GOOGLE_TRANSLATE_ELEMENT_ID;
        element.className = 'skiptranslate';
        element.setAttribute('aria-hidden', 'true');
        document.body.appendChild(element);
    }

    function loadGoogleTranslate(lang) {
        if (lang === 'en') return;

        ensureGoogleTranslateElement();

        window.googleTranslateElementInit = function () {
            if (!window.google || !window.google.translate || !window.google.translate.TranslateElement) return;

            new window.google.translate.TranslateElement({
                pageLanguage: 'en',
                includedLanguages: 'en,hi,ta',
                autoDisplay: false
            }, GOOGLE_TRANSLATE_ELEMENT_ID);

            window.setTimeout(hideGoogleTranslateChrome, 500);
        };

        if (document.querySelector('script[data-google-translate-script]')) return;

        var script = document.createElement('script');
        script.src = 'https://translate.google.com/translate_a/element.js?cb=googleTranslateElementInit';
        script.async = true;
        script.setAttribute('data-google-translate-script', 'true');
        document.head.appendChild(script);
    }

    function initLanguageSwitcher() {
        var lang = currentLanguage();
        updateButtons(lang);
        setGoogleTranslateLanguage(lang);
        loadGoogleTranslate(lang);

        document.querySelectorAll('[data-language-button]').forEach(function (button) {
            button.addEventListener('click', function () {
                changeLanguage(button.getAttribute('data-language-button'));
            });
        });

        hideGoogleTranslateChrome();

        if (window.MutationObserver) {
            new MutationObserver(hideGoogleTranslateChrome).observe(document.documentElement, {
                childList: true,
                subtree: true
            });
        }
    }

    document.addEventListener('DOMContentLoaded', initLanguageSwitcher);
})();

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

        var desktopQuery = window.matchMedia('(min-width: 1200px)');

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

(function () {
    function initChiefGuestsCarousel() {
        document.querySelectorAll('[data-chief-guests-carousel]').forEach(function (carousel) {
            var track = carousel.querySelector('[data-chief-guests-track]');
            var slides = Array.prototype.slice.call(carousel.querySelectorAll('[data-chief-guests-slide]'));
            var dotsWrap = carousel.querySelector('[data-chief-guests-dots]');
            if (!track || !slides.length || !dotsWrap) return;

            var dots = Array.prototype.slice.call(dotsWrap.querySelectorAll('[data-chief-guests-dot]'));
            var activeIndex = 0;
            var timer = null;

            function totalSlides() {
                return slides.length;
            }

            function syncDots() {
                var total = totalSlides();
                dots.forEach(function (dot, index) {
                    var hidden = index >= total;
                    dot.hidden = hidden;
                    dot.classList.toggle('active', !hidden && index === activeIndex);
                    dot.setAttribute('aria-pressed', !hidden && index === activeIndex ? 'true' : 'false');
                });
            }

            function goTo(index) {
                var total = totalSlides();
                activeIndex = (index + total) % total;
                track.style.transform = 'translateX(-' + (activeIndex * 100) + '%)';
                syncDots();
            }

            function start() {
                stop();
                if (totalSlides() <= 1) return;
                timer = window.setInterval(function () {
                    goTo(activeIndex + 1);
                }, 3500);
            }

            function stop() {
                if (!timer) return;
                window.clearInterval(timer);
                timer = null;
            }

            dots.forEach(function (dot) {
                dot.addEventListener('click', function () {
                    var index = parseInt(dot.getAttribute('data-chief-guests-dot'), 10);
                    if (!Number.isFinite(index)) return;
                    goTo(index);
                    start();
                });
            });

            carousel.addEventListener('mouseenter', stop);
            carousel.addEventListener('mouseleave', start);
            carousel.addEventListener('focusin', stop);
            carousel.addEventListener('focusout', start);

            window.addEventListener('resize', function () {
                if (activeIndex >= totalSlides()) activeIndex = 0;
                goTo(activeIndex);
                start();
            });

            goTo(0);
            start();
        });
    }

    document.addEventListener('DOMContentLoaded', initChiefGuestsCarousel);
})();
