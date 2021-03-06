/*
 * Copyright 2013 Simba Open Source
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.simbasecurity.test;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.simbasecurity.core.config.ConfigurationParameter;
import org.simbasecurity.core.config.ConfigurationService;
import org.simbasecurity.core.domain.validator.PasswordValidator;
import org.simbasecurity.core.domain.validator.UserValidator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;


@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:persistenceTestContext.xml")
public abstract class DatabaseTestCase extends LocatorTestCase {

    protected ConfigurationService configurationServiceMock;

    @Before
    public void setUpCommonLocatables() {
        implantMock(UserValidator.class);
        implantMock(PasswordValidator.class);

        configurationServiceMock = implantMock(ConfigurationService.class);
        when(configurationServiceMock.getValue(ConfigurationParameter.DEFAULT_PASSWORD)).thenReturn("aPassword");
    }
}
