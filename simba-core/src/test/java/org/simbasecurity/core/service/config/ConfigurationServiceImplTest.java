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
package org.simbasecurity.core.service.config;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.simbasecurity.core.config.ConfigurationParameter;
import org.simbasecurity.core.config.ConfigurationStore;
import org.simbasecurity.core.config.StoreType;
import org.simbasecurity.core.event.EventService;
import org.simbasecurity.core.event.SimbaEventType;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ConfigurationServiceImplTest {

    private static final String APP_NAME = "app-name";
    private static final List<String> SUCCESS_URLS = Arrays.asList("url-1", "url-2", "url-3");

    @Mock private ConfigurationStore databaseStore;
    @Mock private ConfigurationStore quartzStore;
    @Mock private EventService eventService;

    @InjectMocks
    private ConfigurationServiceImpl configService;

    @Before
    public void setup() {
        EnumMap<StoreType, ConfigurationStore> stores = new EnumMap<StoreType, ConfigurationStore>(StoreType.class);
        stores.put(StoreType.DATABASE, databaseStore);
        stores.put(StoreType.QUARTZ, quartzStore);

        configService.setStores(stores);
    }

    @Test
    public void cachedParameterDoesNotQueryStore() {
        when(databaseStore.getValue(ConfigurationParameter.APPLICATION_NAME)).thenReturn(APP_NAME);
        configService.getValue(ConfigurationParameter.APPLICATION_NAME);

        reset(databaseStore); // Reset because we don't want any mock to be
        // called for cached parameters

        assertEquals(APP_NAME, configService.getValue(ConfigurationParameter.APPLICATION_NAME));
        verifyZeroInteractions(databaseStore, quartzStore);
    }

    @Test
    public void getUniqueParameterReturnsSimpleType() {
        when(databaseStore.getValue(ConfigurationParameter.APPLICATION_NAME)).thenReturn(APP_NAME);

        configService.getValue(ConfigurationParameter.APPLICATION_NAME);

        verify(databaseStore).getValue(ConfigurationParameter.APPLICATION_NAME);
    }

    @Test
    public void getNonUniqueParameterReturnsList() {
        when(databaseStore.getValueList(ConfigurationParameter.SUCCESS_URL)).thenReturn(SUCCESS_URLS);

        configService.getValue(ConfigurationParameter.SUCCESS_URL);

        verify(databaseStore).getValueList(ConfigurationParameter.SUCCESS_URL);
    }

    @Test
    public void changeUniqueParameterStoresSimpleType() {
        configService.changeParameter(ConfigurationParameter.APPLICATION_NAME, APP_NAME);
        verify(databaseStore).setValue(ConfigurationParameter.APPLICATION_NAME, APP_NAME);
        verify(eventService).publish(SimbaEventType.CONFIG_PARAM_CHANGED,
                ConfigurationParameter.APPLICATION_NAME.name(), "SIMBA Manager", APP_NAME);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void changeNonUniqueParameterStoresList() {
        configService.changeParameter(ConfigurationParameter.SUCCESS_URL, SUCCESS_URLS);

        verify(databaseStore).setValueList(eq(ConfigurationParameter.SUCCESS_URL), anyList());
    }
}
