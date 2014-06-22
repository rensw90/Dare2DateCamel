package nl.han.dare2date.service.web.validate;

import nl.han.dare2date.service.web.applyregistration.model.ApplyRegistrationRequest;
import nl.han.dare2date.service.web.applyregistration.model.ApplyRegistrationResponse;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;

/**
 * Created by Rens on 22-6-2014.
 */
public class ValidateCreditCardRoute extends RouteBuilder {
    /**
     * <b>Called on initialization to build the routes using the fluent builder syntax.</b>
     * <p/>
     * This is a central method for RouteBuilder implementations to implement
     * the routes using the Java fluent builder syntax.
     *
     * @throws Exception can be thrown during configuration
     */
    @Override
    public void configure() throws Exception {
        JaxbDataFormat jaxb = new JaxbDataFormat(ApplyRegistrationRequest.class.getPackage().getName());

        from("activemq:queue:request")
                .unmarshal(jaxb)
                .process(new ValidateCreditCard())
                .marshal(jaxb)
                .to("activemq:topic:registration");
    }

    private static final class ValidateCreditCard implements Processor {
        public void process(Exchange exchange) throws Exception {
            ApplyRegistrationResponse registrationResponse = new ApplyRegistrationResponse();
            registrationResponse.setRegistration(exchange.getIn().getBody(ApplyRegistrationRequest.class).getRegistration());
        //    System.out.println("Checking creditcard...");
        //    System.out.println("CVC: "+ registrationResponse.getRegistration().getUser().getCard().getCvc());
        //    System.out.println("Number: "+ registrationResponse.getRegistration().getUser().getCard().getNumber());
            if (registrationResponse.getRegistration().getUser().getCard().getCvc().intValue() == 1234 && registrationResponse.getRegistration().getUser().getCard().getNumber().intValue() == 12345) {
                System.out.println("Creditcard OK.");
                registrationResponse.getRegistration().setSuccesFul(true);
            }

            exchange.getOut().setBody(registrationResponse);
        }
    }
}
