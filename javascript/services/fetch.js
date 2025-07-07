export async function fetchData(url, options) {
    try {
        const response = await fetch(url, options);
        const data = await response.json();
        if (!response.ok) {
            throw new Error(`${data.message}`);
        }
        
        return { data, error: null };
    } catch (error) {
        if(error instanceof TypeError){
            return { data: null, error: "Servidor indisponível." };
        }else{
            return { data: null, error: error.message };
        }
    
        
    }
}