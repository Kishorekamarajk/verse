(() => {
    const form = document.getElementById("exhibitorRegistrationForm");
    const modal = document.getElementById("registrationSuccessModal");
    const modalClose = modal?.querySelector("[data-modal-close]");

    /* ---------------- Multi-step form ---------------- */
    const steps = Array.from(document.querySelectorAll(".xr-step"));
    const progressSteps = Array.from(document.querySelectorAll(".xr-progress-step"));
    let currentStep = 0;

    const showStep = (index, opts = {}) => {
        currentStep = Math.max(0, Math.min(index, steps.length - 1));
        steps.forEach((step, i) => step.classList.toggle("is-active", i === currentStep));
        progressSteps.forEach((el, i) => {
            el.classList.toggle("is-active", i === currentStep);
            el.classList.toggle("is-done", i < currentStep);
        });
        if (opts.scroll !== false) {
            const anchor = document.getElementById("registration-form");
            anchor?.scrollIntoView({ behavior: "smooth", block: "start" });
        }
    };

    const getFieldWrapper = (field) => field.closest("[data-field]");
    const getError = (field) => getFieldWrapper(field)?.querySelector(".xr-field-error");

    const setError = (field, message) => {
        const wrapper = getFieldWrapper(field);
        const error = getError(field);
        wrapper?.querySelector(".xr-field")?.classList.add("has-error");
        field.setAttribute("aria-invalid", "true");
        if (error) {
            error.textContent = message;
            error.classList.add("is-visible");
        }
    };

    const clearError = (field) => {
        const wrapper = getFieldWrapper(field);
        const error = getError(field);
        wrapper?.querySelector(".xr-field")?.classList.remove("has-error");
        field.removeAttribute("aria-invalid");
        if (error) {
            error.textContent = "";
            error.classList.remove("is-visible");
        }
    };

    const isValidUrl = (value) => {
        try {
            const url = new URL(value);
            return url.protocol === "http:" || url.protocol === "https:";
        } catch {
            return false;
        }
    };

    const GST_PATTERN = /^\d{2}[A-Z]{5}\d{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$/;

    const validateField = (field) => {
        if (!field.id) return true;
        clearError(field);
        const value = field.value.trim();
        const validator = field.dataset.validate;

        if (field.required && !value) {
            setError(field, "This field is required.");
            return false;
        }

        if (!value) return true;

        if (validator === "email" && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value)) {
            setError(field, "Please enter a valid email address.");
            return false;
        }

        if (validator === "indian-mobile" && !/^(?:\+91[-\s]?)?[6-9]\d{9}$/.test(value)) {
            setError(field, "Please enter a valid Indian mobile number.");
            return false;
        }

        if (validator === "optional-url" && !isValidUrl(value)) {
            setError(field, "Please enter a valid URL starting with http:// or https://.");
            return false;
        }

        if (validator === "optional-gst" && !GST_PATTERN.test(value.toUpperCase())) {
            setError(field, "Please enter a valid 15-character GSTIN.");
            return false;
        }

        return true;
    };

    const validateStep = (index) => {
        const fields = Array.from(steps[index]?.querySelectorAll("input, select, textarea") || []);
        const results = fields.map(validateField);
        return results.every(Boolean);
    };

    const focusFirstInvalid = (index) => {
        const firstInvalid = steps[index]?.querySelector("[aria-invalid='true']");
        firstInvalid?.focus();
    };

    document.querySelectorAll("[data-step-next]").forEach((btn) => {
        btn.addEventListener("click", () => {
            if (validateStep(currentStep)) {
                showStep(currentStep + 1);
            } else {
                focusFirstInvalid(currentStep);
            }
        });
    });

    document.querySelectorAll("[data-step-prev]").forEach((btn) => {
        btn.addEventListener("click", () => showStep(currentStep - 1));
    });

    /* ---------------- Package quick-select ---------------- */
    document.querySelectorAll("[data-select-package]").forEach((btn) => {
        btn.addEventListener("click", () => {
            const select = document.getElementById("stallPackage");
            if (select) select.value = btn.dataset.selectPackage;
            showStep(0);
        });
    });

    /* ---------------- Modal ---------------- */
    const openModal = () => {
        modal?.classList.remove("hidden");
        modal?.classList.add("flex");
        modalClose?.focus();
    };

    const closeModal = () => {
        modal?.classList.add("hidden");
        modal?.classList.remove("flex");
    };

    modalClose?.addEventListener("click", closeModal);
    modal?.addEventListener("click", (event) => {
        if (event.target === modal) closeModal();
    });
    document.addEventListener("keydown", (event) => {
        if (event.key === "Escape") closeModal();
    });

    /* ---------------- Form submit / reset / live validation ---------------- */
    form?.addEventListener("submit", (event) => {
        event.preventDefault();
        const lastStep = steps.length - 1;

        for (let i = 0; i <= lastStep; i += 1) {
            if (!validateStep(i)) {
                showStep(i);
                focusFirstInvalid(i);
                return;
            }
        }

        openModal();
        form.reset();
        showStep(0, { scroll: false });
    });

    form?.addEventListener("reset", () => {
        form.querySelectorAll("input, select, textarea").forEach(clearError);
    });

    form?.addEventListener("input", (event) => {
        if (event.target.matches("input, textarea")) {
            validateField(event.target);
        }
    });

    form?.addEventListener("change", (event) => {
        if (event.target.matches("select")) {
            validateField(event.target);
        }
    });

    if (steps.length) showStep(0, { scroll: false });

    /* ---------------- FAQ accordion ---------------- */
    document.querySelectorAll(".xr-faq-question").forEach((btn) => {
        btn.addEventListener("click", () => {
            const item = btn.closest(".xr-faq-item");
            if (!item) return;
            const answer = item.querySelector(".xr-faq-answer");
            const wasOpen = item.classList.contains("is-open");

            document.querySelectorAll(".xr-faq-item.is-open").forEach((openItem) => {
                if (openItem !== item) {
                    openItem.classList.remove("is-open");
                    const openAnswer = openItem.querySelector(".xr-faq-answer");
                    if (openAnswer) openAnswer.style.maxHeight = "";
                }
            });

            item.classList.toggle("is-open", !wasOpen);
            if (answer) answer.style.maxHeight = wasOpen ? "" : `${answer.scrollHeight}px`;
        });
    });

    /* ---------------- Hero / CTA stat counters ---------------- */
    const counters = document.querySelectorAll("[data-xr-counter]");
    if (counters.length) {
        const reduceMotion = window.matchMedia && window.matchMedia("(prefers-reduced-motion: reduce)").matches;

        const animateCounter = (el) => {
            const target = parseInt(el.dataset.xrCounter, 10) || 0;
            const duration = 1400;
            let start = null;

            const step = (timestamp) => {
                if (start === null) start = timestamp;
                const progress = Math.min((timestamp - start) / duration, 1);
                const eased = 1 - (1 - progress) * (1 - progress);
                el.textContent = Math.round(target * eased).toLocaleString("en-IN");
                if (progress < 1) {
                    requestAnimationFrame(step);
                } else {
                    el.textContent = target.toLocaleString("en-IN");
                }
            };

            requestAnimationFrame(step);
        };

        if (reduceMotion || !("IntersectionObserver" in window)) {
            counters.forEach((el) => { el.textContent = (parseInt(el.dataset.xrCounter, 10) || 0).toLocaleString("en-IN"); });
        } else {
            const observer = new IntersectionObserver((entries, obs) => {
                entries.forEach((entry) => {
                    if (entry.isIntersecting) {
                        animateCounter(entry.target);
                        obs.unobserve(entry.target);
                    }
                });
            }, { threshold: 0.4 });

            counters.forEach((el) => observer.observe(el));
        }
    }
})();
