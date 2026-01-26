export async function fetchData(url, options = {}) {
    try {
        const response = await fetch(url, options);

        let responseData = null;
        const contentType = response.headers.get('content-type');
        if (contentType && contentType.includes('application/json')) {
            responseData = await response.json();
        }

        if (!response.ok) {
            const errorMessage = 
                responseData?.mensagem ||
                responseData?.message ||
                responseData?.erro ||
                `Erro ${response.status}: ${response.statusText}`;

            return { 
                data: null, 
                error: { 
                    message: errorMessage, 
                    status: response.status,
                    errosValidacao: responseData?.errosValidacao 
                } 
            };
        }

        return { data: responseData, error: null };

    } catch (networkError) {
        console.error("Erro de rede:", networkError);
        return { 
            data: null, 
            error: { 
                message: "Não foi possível conectar ao servidor. Verifique sua conexão.", 
                status: 0 
            } 
        };
    }
}