/*
 * Copyright (c) 2010-2011. Axon Framework
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.axonframework.contextsupport.spring;

import org.axonframework.saga.repository.jpa.JpaSagaRepository;
import org.axonframework.saga.spring.SpringResourceInjector;
import org.axonframework.serializer.XStreamSerializer;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * Bean definition parser that parses &lt;jpa-saga-repository&gt; elements into Spring bean definitions.
 *
 * @author Allard Buijze
 * @since 1.1
 */
public class JpaSagaRepositoryBeanDefinitionParser extends AbstractBeanDefinitionParser {

    private static final String RESOURCE_INJECTOR_ATTRIBUTE = "resource-injector";
    private static final String EXPLICIT_FLUSH_ATTRIBUTE = "use-explicit-flush";
    private static final String SAGA_SERIALIZER_ATTRIBUTE = "saga-serializer";
    private static final String SAGA_SERIALIZER_PROPERTY = "serializer";

    @Override
    protected AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(JpaSagaRepository.class);
        parseResourceInjectorAttribute(element, builder);
        parseExplicitFlushAttribute(element, builder);
        parseSagaSerializerAttribute(element, builder);
        return builder.getBeanDefinition();
    }

    private void parseSagaSerializerAttribute(Element element, BeanDefinitionBuilder beanDefinition) {
        if (element.hasAttribute(SAGA_SERIALIZER_ATTRIBUTE)) {
            beanDefinition.addPropertyReference(SAGA_SERIALIZER_PROPERTY,
                                                element.getAttribute(SAGA_SERIALIZER_ATTRIBUTE));
        } else {
            GenericBeanDefinition defaultSerializer = new GenericBeanDefinition();
            defaultSerializer.setBeanClass(XStreamSerializer.class);
            beanDefinition.addPropertyValue(SAGA_SERIALIZER_PROPERTY, defaultSerializer);
        }
    }

    private void parseExplicitFlushAttribute(Element element, BeanDefinitionBuilder beanDefinition) {
        if (element.hasAttribute(EXPLICIT_FLUSH_ATTRIBUTE)) {
            beanDefinition.addPropertyValue("useExplicitFlush", element.getAttribute(EXPLICIT_FLUSH_ATTRIBUTE));
        }
    }

    private void parseResourceInjectorAttribute(Element element, BeanDefinitionBuilder beanDefinition) {
        if (element.hasAttribute(RESOURCE_INJECTOR_ATTRIBUTE)) {
            beanDefinition.addPropertyReference("resourceInjector", element.getAttribute(RESOURCE_INJECTOR_ATTRIBUTE));
        } else {
            GenericBeanDefinition defaultResourceInjector = new GenericBeanDefinition();
            defaultResourceInjector.setBeanClass(SpringResourceInjector.class);
            beanDefinition.addPropertyValue("resourceInjector", defaultResourceInjector);
        }
    }
}
