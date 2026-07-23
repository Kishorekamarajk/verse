(() => {
    const form = document.getElementById("exhibitorRegistrationForm");
    const modal = document.getElementById("registrationSuccessModal");
    const modalClose = modal?.querySelector("[data-modal-close]");

    /* ---------------- Backend integration helpers ---------------- */
    const submitButton = form?.querySelector('button[type="submit"]');
    const submitButtonDefaultHtml = submitButton?.innerHTML;

    const setSubmitting = (isSubmitting) => {
        if (!submitButton) return;
        submitButton.disabled = isSubmitting;
        submitButton.setAttribute("aria-busy", String(isSubmitting));
        submitButton.innerHTML = isSubmitting
            ? '<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Submitting...'
            : submitButtonDefaultHtml;
    };

    const showFormError = (message) => {
        if (!form) return;
        let banner = form.querySelector("#formSubmitError");
        if (!banner) {
            banner = document.createElement("p");
            banner.id = "formSubmitError";
            banner.className = "xr-field-error is-visible";
            banner.setAttribute("role", "alert");
            banner.style.textAlign = "center";
            banner.style.marginBottom = "1rem";
            form.insertBefore(banner, form.firstChild);
        }
        banner.textContent = message;
    };

    const clearFormError = () => {
        form?.querySelector("#formSubmitError")?.remove();
    };

    const debounce = (fn, delay = 400) => {
        let timer;
        return (...args) => {
            clearTimeout(timer);
            timer = setTimeout(() => fn(...args), delay);
        };
    };

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
    const LINKEDIN_PATTERN = /^https?:\/\/([a-zA-Z]{2,3}\.)?linkedin\.com\/.*$/i;

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

        if (field.id === "linkedin" && !LINKEDIN_PATTERN.test(value)) {
            setError(field, "Please enter a valid LinkedIn profile URL.");
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

    /* ---------------- AJAX duplicate email/phone check ---------------- */
    const checkDuplicate = async (field, endpoint, paramName) => {
        const value = field.value.trim();
        if (!value || !validateField(field)) return;
        try {
            const response = await fetch(`${endpoint}?${paramName}=${encodeURIComponent(value)}`, {
                headers: { Accept: "application/json" },
            });
            if (!response.ok) return;
            const body = await response.json();
            if (body?.data === true) {
                setError(field, body.message || "This value is already registered.");
            }
        } catch {
            /* Non-fatal: the authoritative duplicate check still runs server-side on submit. */
        }
    };

    const emailField = document.getElementById("email");
    const phoneField = document.getElementById("phone");
    emailField?.addEventListener("blur", debounce(() => checkDuplicate(emailField, "/check-email", "email"), 200));
    phoneField?.addEventListener("blur", debounce(() => checkDuplicate(phoneField, "/check-phone", "phone"), 200));

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
    let isSubmitting = false;

    form?.addEventListener("submit", async (event) => {
        event.preventDefault();
        if (isSubmitting) return;

        const lastStep = steps.length - 1;
        for (let i = 0; i <= lastStep; i += 1) {
            if (!validateStep(i)) {
                showStep(i);
                focusFirstInvalid(i);
                return;
            }
        }

        clearFormError();
        isSubmitting = true;
        setSubmitting(true);

        const payload = Object.fromEntries(new FormData(form).entries());

        try {
            const response = await fetch("/register", {
                method: "POST",
                headers: { "Content-Type": "application/json", Accept: "application/json" },
                body: JSON.stringify(payload),
            });
            const result = await response.json().catch(() => null);

            if (response.ok && result?.success) {
                openModal();
                form.reset();
                showStep(0, { scroll: false });
            } else if (response.status === 400 && result?.data && typeof result.data === "object") {
                Object.entries(result.data).forEach(([fieldName, message]) => {
                    const field = form.querySelector(`[name="${fieldName}"]`);
                    if (field) setError(field, message);
                });
                const firstInvalidStep = steps.findIndex((step) => step.querySelector("[aria-invalid='true']"));
                if (firstInvalidStep >= 0) {
                    showStep(firstInvalidStep);
                    focusFirstInvalid(firstInvalidStep);
                }
            } else if (response.status === 409) {
                const message = result?.message || "This registration already exists.";
                const targetField = /email/i.test(message) ? emailField : /phone/i.test(message) ? phoneField : null;
                if (targetField) {
                    showStep(1);
                    setError(targetField, message);
                    targetField.focus();
                } else {
                    showFormError(message);
                }
            } else {
                showFormError(result?.message || "Something went wrong while submitting your application. Please try again.");
            }
        } catch {
            showFormError("Unable to reach the server. Please check your connection and try again.");
        } finally {
            isSubmitting = false;
            setSubmitting(false);
        }
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
