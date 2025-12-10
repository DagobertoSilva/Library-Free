package libraryfree.app.auth.strategy;

import libraryfree.app.enumerator.TipoUsuario;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class LoginFactory {
    private Map<TipoUsuario, LoginStrategy> strategies = new EnumMap<>(TipoUsuario.class);

    public LoginFactory(List<LoginStrategy> strategyList) {
        strategyList.forEach(s -> strategies.put(s.getTipo(), s));
    }

    public LoginStrategy getStrategy(TipoUsuario tipo){
        return strategies.get(tipo);
    }
}
