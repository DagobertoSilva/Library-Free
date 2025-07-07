export async function fetchData(url, options) {
    try {
        const response = await fetch(url, options);
        const json = await response.json();
        if (!response.ok) {
            throw new Error(`${json.message}`);
        }
        return { data: json, error: null };
    } catch (error) {
        return {json: null, error: error.message}
    }
}