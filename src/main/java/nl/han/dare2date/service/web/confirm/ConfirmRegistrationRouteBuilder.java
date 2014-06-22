package nl.han.dare2date.service.web.confirm;

/**
 * Created by Rens on 18-6-2014.
 */
public class ConfirmRegistrationRouteBuilder {
    public static void main(String[] args) throws Exception {
        org.apache.camel.main.Main main = new org.apache.camel.main.Main();
        main.enableHangupSupport();
        main.addRouteBuilder(new ConfirmRegistrationRoute());
        main.run();
    }
}
