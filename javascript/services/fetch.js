export async function fetchData(url, options) {
    console.log("DEBUG: fetchData recebendo URL:", url);
    try {
        const response = await fetch(url, options);

        if (response.status === 401 || response.status === 403) {
            window.location.href = './login.html';
            return { data: null, error: new Error("Sessão expirada ou não autorizado.") };
        }

        if (!response.ok) {
            let errorJson = null;
            try {
                errorJson = await response.json();
            } catch (e) {
                throw new Error(`Erro ${response.status}: ${response.statusText}`);
            }
            throw new Error(errorJson.message || `Erro ${response.status}: ${response.statusText}`);
        }

        const contentType = response.headers.get('content-type');
        if (response.status !== 204 && contentType && contentType.includes('application/json')) {
            const json = await response.json();
            return { data: json, error: null };
        } else if (response.status === 204) {
            return { data: null, error: null };
        } else {
            console.warn(`Requisição ${url} retornou 200 OK, mas sem content-type JSON ou 204 No Content.`);
            return { data: null, error: null };
        }

    } catch (error) {
        return { data: null, error: error }; 
    }
}