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

import javax.portlet.ActionRequest;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.liferay.blogs.kernel.model.BlogsEntry;
import com.liferay.blogs.kernel.service.BlogsEntryLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.template.Template;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;

import blogs.web.constants.BlogsWebPortletKeys;

/**
 * @author Bruno Basto
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + BlogsWebPortletKeys.BlogsWeb,
		"mvc.command.name=Edit"
	},
	service = MVCRenderCommand.class
)
public class BlogsWebEditMVCRenderCommand implements MVCRenderCommand {

	@Override
	public String render(
		RenderRequest renderRequest, RenderResponse renderResponse) {

		Template template = (Template)renderRequest.getAttribute(
			WebKeys.TEMPLATE);

		PortletURL addBlogUrl = renderResponse.createActionURL();

		long blogEntryId = ParamUtil.getLong(renderRequest, "blogEntryId");

		boolean editMode = false;

		if (blogEntryId > 0) {
			editMode = true;
		}

		template.put("editMode", editMode);

		if (editMode) {
			try {
				BlogsEntry blogsEntry = _blogsEntryLocalService.getEntry(
					blogEntryId);

				template.put("content", blogsEntry.getContent());
				template.put("title", blogsEntry.getTitle());

				addBlogUrl.setParameter(ActionRequest.ACTION_NAME, "Edit");
				addBlogUrl.setParameter(
					"blogEntryId", String.valueOf(blogEntryId));
			} catch (PortalException e) {
				e.printStackTrace();
			}
		}
		else {
			addBlogUrl.setParameter(ActionRequest.ACTION_NAME, "Save");
		}

		template.put("saveBlogUrl", addBlogUrl.toString());

		// Dispatch to the Edit soy namespace
		return "Edit";
	}

	@Reference
	private BlogsEntryLocalService _blogsEntryLocalService;

}