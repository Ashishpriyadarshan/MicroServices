package com.micro.accounts.audit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditAwareImpl")
public class AuditAwareImpl implements AuditorAware<String> {
    /**
     * Returns the current auditor of the application.
     *
     * @return the current auditor.
     * The below method is responsible to handle who is the current auditor or in our case we have the baseEntity
     * where there are certain fields that needs to be populated everytime a row of the DB is created or updated so
     * the information like createdtime and updatedtime we can get but not who created and who updated so for that reason we have this
     * function here that is responsible to return both the fields(CreatedBy and UpdatedBy) , inside this function we write the logic by which we can extract the information
     * of who is the current user/auditor it is mostly extracted using the the authenticated user info but for now since we are not using
     * spring security so we will hardcode the value.
     * in the above implements part we have <String> because the fields we have are of type String if you check the BaseEntity class</String>
     */
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of("ACCOUNTS_ADMIN");
    }
}
