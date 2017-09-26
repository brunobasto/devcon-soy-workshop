/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package blogs.web.portlet.action;

import blogs.web.constants.BlogsWebPortletKeys;

import com.liferay.blogs.kernel.service.BlogsEntryLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.StringPool;

import java.util.Date;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletURL;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Bruno Basto
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + BlogsWebPortletKeys.BlogsWeb,
		"mvc.command.name=Save"
	},
	service = MVCActionCommand.class
)
public class BlogsWebSaveMVCActionCommand extends BaseMVCActionCommand {

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		String title = ParamUtil.getString(actionRequest, "title");
		String content = ParamUtil.getString(actionRequest, "content");

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			actionRequest);

		long userId = PortalUtil.getUserId(actionRequest);

		LiferayPortletResponse liferayPortletResponse =
			_portal.getLiferayPortletResponse(actionResponse);

		PortletURL portletURL = liferayPortletResponse.createRenderURL();

		try {
			_blogsEntryLocalService.addEntry(
				userId, title, StringPool.BLANK, StringPool.BLANK, content,
				new Date(), false, false, null, StringPool.BLANK, null, null,
				serviceContext);

			portletURL.setParameter("mvcRenderCommandName", "View");
		}
		catch (PortalException pe) {
			SessionErrors.add(
				actionRequest, "Sorry, an error occurred with your request.");

			portletURL.setParameter("mvcRenderCommandName", "Edit");
		}

		sendRedirect(actionRequest, actionResponse, portletURL.toString());
	}

	@Reference
	private BlogsEntryLocalService _blogsEntryLocalService;

	@Reference
	private Portal _portal;

}