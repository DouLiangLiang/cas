package org.apereo.cas.authentication.handler;

import org.apereo.cas.authentication.AuthenticationHandler;
import org.apereo.cas.authentication.AuthenticationHandlerResolver;
import org.apereo.cas.authentication.AuthenticationTransaction;
import org.apereo.cas.authentication.UsernamePasswordCredential;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This is {@link ByCredentialSourceAuthenticationHandlerResolver}
 * that attempts to capture the source from the credential
 * and limit the handlers to the matching source.
 *
 * @author Misagh Moayyed
 * @since 6.0.0
 */
@Slf4j
@RequiredArgsConstructor
public class ByCredentialSourceAuthenticationHandlerResolver implements AuthenticationHandlerResolver {

    @Override
    public boolean supports(final Set<AuthenticationHandler> handlers, final AuthenticationTransaction transaction) {
        return transaction.hasCredentialOfType(UsernamePasswordCredential.class);
    }

    @Override
    public Set<AuthenticationHandler> resolve(final Set<AuthenticationHandler> candidateHandlers, final AuthenticationTransaction transaction) {
        val finalHandlers = new LinkedHashSet<AuthenticationHandler>();
        val upcs = transaction.getCredentialsOfType(UsernamePasswordCredential.class);
        candidateHandlers
            .stream()
            .filter(handler -> handler.supports(UsernamePasswordCredential.class))
            .filter(handler -> {
                final String handlerName = handler.getName();
                LOGGER.debug("Evaluating authentication handler [{}] for eligibility", handlerName);
                return upcs.stream().anyMatch(c -> {
                    LOGGER.debug("Comparing credential source [{}] against authentication handler [{}]", c.getSource(), handlerName);
                    return c.getSource().equalsIgnoreCase(handlerName);
                });
            })
            .forEach(finalHandlers::add);
        return finalHandlers;
    }
}
