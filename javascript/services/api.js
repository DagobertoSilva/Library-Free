const API_URL = 'http://localhost:8080/libraryfree';

export function LOGIN(body){
    return{
        url: `${API_URL}/auth/login`,
        options:{
            method: 'POST',
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(body)
        }
    }
}