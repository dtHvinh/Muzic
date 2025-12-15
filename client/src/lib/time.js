export const formatDate = (
    input,
    {
        locale = "en-US",
        format = "short", // "short" | "long" | "full"
        includeTime = false,
    } = {}
) => {
    if (!input) return "";

    let date;

    // 1️⃣ Java LocalDateTime array → JS Date
    if (Array.isArray(input)) {
        const [
            year,
            month,
            day,
            hour = 0,
            minute = 0,
            second = 0,
            nano = 0,
        ] = input;

        date = new Date(
            year,
            month - 1,
            day,
            hour,
            minute,
            second,
            Math.floor(nano / 1_000_000)
        );
    }
    // 2️⃣ ISO string or timestamp
    else {
        date = new Date(input);
    }

    if (isNaN(date.getTime())) return "";

    const baseOptions =
        format === "full"
            ? { dateStyle: "full" }
            : format === "long"
                ? { dateStyle: "long" }
                : { month: "short", day: "numeric", year: "numeric" };

    const timeOptions = includeTime
        ? { hour: "2-digit", minute: "2-digit" }
        : {};

    return new Intl.DateTimeFormat(locale, {
        ...baseOptions,
        ...timeOptions,
    }).format(date);
};
