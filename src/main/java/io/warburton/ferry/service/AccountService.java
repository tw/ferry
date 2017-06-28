package io.warburton.ferry.service;

import io.warburton.ferry.model.Account;

import java.util.UUID;

/**
 * @author tw
 */
public interface AccountService {

    Account get(UUID id);

}
