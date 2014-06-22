package nl.han.dare2date.service.web.validate;

/**
 * Created by Rens on 22-6-2014.
 */
public class ValidateCreditCardRouteBuilder {
    public static void main(String[] args) throws Exception {
        org.apache.camel.main.Main main = new org.apache.camel.main.Main();
        main.enableHangupSupport();
        main.addRouteBuilder(new ValidateCreditCardRoute());
        main.run();
    }
}

