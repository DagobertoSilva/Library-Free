package libraryfree.app.dto.erro;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public record ErroResponse(
        Instant timestamp,
        int status,
        String erro,
        String mensagem,
        Map<String, List<String>> errosValidacao,
        String path
) { }
