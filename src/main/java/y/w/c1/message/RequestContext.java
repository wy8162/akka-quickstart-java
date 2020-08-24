package y.w.c1.message;

import lombok.AccessLevel;
import lombok.Setter;

@Setter(value = AccessLevel.NONE)
public class RequestContext implements Command {
    private static final long serialVersionUID = -3950980492198527646L;

    private boolean CCAV6Enabled;
    private boolean retrueveCustomerRoleFromAccountHub;
}
