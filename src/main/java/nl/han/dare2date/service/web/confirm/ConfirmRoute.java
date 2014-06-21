package nl.han.dare2date.service.web.confirm;

import nl.han.dare2date.service.web.applyregistration.model.ApplyRegistrationRequest;
import nl.han.dare2date.service.web.applyregistration.model.ApplyRegistrationResponse;
import nl.han.dare2date.service.web.applyregistration.model.Creditcard;
import nl.han.dare2date.service.web.applyregistration.model.Registration;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;

/**
 * Created by Rens on 18-6-2014.
 */
public class ConfirmRoute extends RouteBuilder {
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
                .unmarshal(jaxb).
                log("${body}").
                process(new Echo())
                .marshal(jaxb).to("activemq:queue:response");
    }

    private static final class Echo implements Processor {
        public void process(Exchange exchange) throws Exception {
            ApplyRegistrationResponse registrationResponse = new ApplyRegistrationResponse();
            registrationResponse.setRegistration(exchange.getIn().getBody(ApplyRegistrationRequest.class).getRegistration());
            Registration registration = exchange.getIn().getBody(Registration.class);
            Creditcard creditcard = exchange.getIn().getBody(Creditcard.class);

            System.out.println("TESTESTSESTSETSET");

            if (creditcard.getCvc().equals(1234) && creditcard.getNumber().equals(12345)) {
                registration.setSuccesFul(true);
                registrationResponse.setRegistration(registration);

            }

            exchange.getOut().setBody(registrationResponse);
        }
    }
}
