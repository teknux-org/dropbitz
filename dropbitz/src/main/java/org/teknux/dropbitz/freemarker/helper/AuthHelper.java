/*
 * Copyright (C) 2014 TekNux.org
 *
 * This file is part of the dropbitz Community GPL Source Code.
 *
 * dropbitz Community Source Code is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * dropbitz Community Source Code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with dropbitz Community Source Code.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.teknux.dropbitz.freemarker.helper;

import freemarker.template.TemplateModelException;
import org.teknux.dropbitz.model.view.IModel;
import org.teknux.dropbitz.provider.AuthenticationHelper;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class AuthHelper extends AbstractHelper {

    private AuthenticationHelper authenticationHelper;

    public AuthHelper() {
        this(new AuthenticationHelper());
    }

    public AuthHelper(AuthenticationHelper authenticationHelper) {
        this.authenticationHelper = authenticationHelper;
    }

    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
        if (arguments.size() != 0) {
            throw new TemplateModelException("Do not specify argument");
        }

        IModel iModel = getIModel();

        HttpServletRequest httpServletRequest = iModel.getHttpServletRequest();
        if (httpServletRequest == null) {
            throw new TemplateModelException("Can not get HttpServletRequest");
        }

        return authenticationHelper.getAuth(httpServletRequest);
    }
}
