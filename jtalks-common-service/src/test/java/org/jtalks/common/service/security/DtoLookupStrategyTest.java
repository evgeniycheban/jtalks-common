/**
 * Copyright (C) 2011  jtalks.org Team
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 * Also add information on how to contact you by electronic and paper mail.
 * Creation date: Apr 12, 2011 / 8:05:19 PM
 * The jtalks.org Project
 */
package org.jtalks.common.service.security;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Sid;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;

/**
 * <p>This class contains unit tests for {@link DtoLookupStrategy}.</p>
 * Date: 16.09.2011<br />
 * Time: 16:06
 *
 * @author Alexey Malev
 */
public class DtoLookupStrategyTest {

    private DtoMapper mapper;
    private LookupStrategy strategy;

    private DtoLookupStrategy sut;

    private class c1 {

    }

    private class c2 {

    }

    @BeforeMethod
    public void setUp() {
        mapper = mock(DtoMapper.class);
        strategy = mock(LookupStrategy.class);
        when(strategy.readAclsById(anyListOf(ObjectIdentity.class), anyListOf(Sid.class))).thenAnswer(
              new Answer<Map<ObjectIdentity, Acl>>() {
                  @Override
                  public Map<ObjectIdentity, Acl> answer(InvocationOnMock invocation) throws Throwable {
                      List<ObjectIdentity> firstArgument = (List<ObjectIdentity>) invocation.getArguments()[0];
                      Map<ObjectIdentity, Acl> result = new HashMap<ObjectIdentity, Acl>();
                      result.put(firstArgument.get(0), null);

                      return result;
                  }
              });

        sut = new DtoLookupStrategy(strategy, mapper);
    }

    @Test
    public void testReadAclsByIdUsingMapping() {
        try {
            when(mapper.getMapping(c1.class.getCanonicalName())).thenReturn(c2.class);

            ObjectIdentity identity = mock(ObjectIdentity.class);
            when(identity.getType()).thenReturn(c1.class.getCanonicalName());
            when(identity.getIdentifier()).thenReturn(1L);

            List<ObjectIdentity> objects = new ArrayList<ObjectIdentity>();
            objects.add(identity);

            List<Sid> sids = new ArrayList<Sid>();

            Map<ObjectIdentity, Acl> result = sut.readAclsById(objects, sids);

            verify(strategy).readAclsById(anyListOf(ObjectIdentity.class), anyListOf(Sid.class));
            assertEquals(result.keySet().iterator().next().getType(), c2.class.getCanonicalName());
        }
        catch (ClassNotFoundException e) {
            throw new IllegalStateException("ClassNotFoundException shouldn't be thrown here", e);
        }
    }

    @Test
    public void testReadAclsByIdWithoutMapping() {
        try {
            when(mapper.getMapping(c1.class.getCanonicalName())).thenReturn(c1.class);


            ObjectIdentity identity = mock(ObjectIdentity.class);
            when(identity.getType()).thenReturn(c1.class.getCanonicalName());

            List<ObjectIdentity> objects = new ArrayList<ObjectIdentity>();
            objects.add(identity);

            List<Sid> sids = new ArrayList<Sid>();

            Map<ObjectIdentity, Acl> result = sut.readAclsById(objects, sids);

            verify(strategy).readAclsById(anyListOf(ObjectIdentity.class), anyListOf(Sid.class));
            assertEquals(result.keySet().iterator().next().getType(), c1.class.getCanonicalName());
        }
        catch (ClassNotFoundException e) {
            throw new IllegalStateException("ClassNotFoundException shouldn't be thrown here", e);
        }
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testReadAclByIdWithBadIdentity() {
        try {
            when(mapper.getMapping(c1.class.getCanonicalName())).thenThrow(
                  new ClassNotFoundException("Class not found"));

            ObjectIdentity identity = mock(ObjectIdentity.class);
            when(identity.getType()).thenReturn(c1.class.getCanonicalName());

            List<ObjectIdentity> objects = new ArrayList<ObjectIdentity>();
            objects.add(identity);

            List<Sid> sids = new ArrayList<Sid>();

            Map<ObjectIdentity, Acl> result = sut.readAclsById(objects, sids);
        }
        catch (ClassNotFoundException e) {
            throw new IllegalStateException("ClassNotFoundException shouldn't be thrown here", e);
        }
    }
}
