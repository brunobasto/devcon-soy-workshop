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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.liferay.blogs.kernel.model.BlogsEntry;
import com.liferay.blogs.kernel.service.BlogsEntryLocalService;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.template.Template;
import com.liferay.portal.kernel.util.WebKeys;

import blogs.web.constants.BlogsWebPortletKeys;

/**
 * @author Bruno Basto
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + BlogsWebPortletKeys.BlogsWeb,
		"mvc.command.name=/", "mvc.command.name=View"
	},
	service = MVCRenderCommand.class
)
public class BlogsWebViewMVCRenderCommand implements MVCRenderCommand {

	@Override
	public String render(
		RenderRequest renderRequest, RenderResponse renderResponse) {

		Template template = (Template)renderRequest.getAttribute(
			WebKeys.TEMPLATE);

		List<BlogsEntry> blogs = _blogsEntryLocalService.getBlogsEntries(
			QueryUtil.ALL_POS, QueryUtil.ALL_POS);

		template.put(
			"blogs",
			blogs
				.stream()
				.map(blog -> _blogMapper(blog, renderResponse))
				.collect(Collectors.toList()));
		
		// Dispatch to the View soy namespace
		return "View";
	}

	private Map<String, Object> _blogMapper(
		BlogsEntry blog, RenderResponse renderResponse) {

		Map<String, Object> blogTemplateContext = new HashMap<>();

		long userId = blog.getUserId();

		try {
			User author = _userLocalService.getUserById(userId);

			blogTemplateContext.put("authorEmail", author.getEmailAddress());
			blogTemplateContext.put("authorInitials", author.getInitials());
			blogTemplateContext.put("authorName", author.getFullName());
		}
		catch (PortalException pe) {
			pe.printStackTrace();
		}

		blogTemplateContext.put("title", blog.getTitle());

		return blogTemplateContext;
	}

	@Reference
	private BlogsEntryLocalService _blogsEntryLocalService;

	@Reference
	private UserLocalService _userLocalService;

}