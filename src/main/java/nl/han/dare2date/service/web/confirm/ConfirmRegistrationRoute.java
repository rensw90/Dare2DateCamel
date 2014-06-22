package nl.han.dare2date.service.web.confirm;

import nl.han.dare2date.service.web.applyregistration.model.ApplyRegistrationRequest;
import nl.han.dare2date.service.web.applyregistration.model.ApplyRegistrationResponse;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;

/**
 * Created by Rens on 18-6-2014.
 */
public class ConfirmRegistrationRoute extends RouteBuilder {
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
        JaxbDataFormat jaxb = new JaxbDataFormat(ApplyRegistrationResponse.class.getPackage().getName());

        from("activemq:topic:registration")
                .unmarshal(jaxb)
                .log("${body}")
                .process(new Echo())
                .marshal(jaxb)
                .to("activemq:queue:response")
                .to("spring-ws:http://localhost:8080/Dare2DateCamel-1.0-SNAPSHOT/applyregistration");
    }

    private static final class Echo implements Processor {
        public void process(Exchange exchange) throws Exception {
            ApplyRegistrationResponse registrationResponse = new ApplyRegistrationResponse();
            registrationResponse.setRegistration(exchange.getIn().getBody(ApplyRegistrationResponse.class).getRegistration());

            if (registrationResponse.getRegistration().isSuccesFul()) {
                System.out.println("Registration succesful");
                exchange.getOut().setBody(registrationResponse);
            }
            else{
                System.out.println("Registration unsuccesful");
            }

        }
    }
}
