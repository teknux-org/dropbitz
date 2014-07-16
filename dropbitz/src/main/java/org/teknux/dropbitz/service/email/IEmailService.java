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

package org.teknux.dropbitz.service.email;

import org.teknux.dropbitz.model.view.IModel;
import org.teknux.dropbitz.service.IService;


/**
 * Service sending emails
 */
public interface IEmailService extends
		IService {

	/**
	 * Sends an HTML email providing a template name, without model for the template.
	 * 
	 * @param subject
	 *            Email Subject
	 * @param viewName
	 *            Freemarker Template Name
	 */
	public void sendEmail(String subject, String viewName);

	/**
	 * Sends an HTML email providing a template name and the model for the template.
	 * 
	 * @param subject
	 *            Email Subject
	 * @param viewName
	 *            Freemarker Template Name
	 * @param model
	 *            Object model for Freemarker template
	 */
	public void sendEmail(String subject, String viewName, IModel model);

	/**
	 * Sends an HTML email providing a template name, the model for the template and an alternative fallback non-HTML
	 * template.
	 * 
	 * @param subject
	 *            Email Subject
	 * @param viewName
	 *            Freemarker Template Name
	 * @param model
	 *            Object model for Freemarker template
	 * @param viewNameAlt
	 *            Alternative Freemarker Template Name (Non-HTML)
	 */
	public void sendEmail(String subject, String viewName, IModel model, String viewNameAlt);

}