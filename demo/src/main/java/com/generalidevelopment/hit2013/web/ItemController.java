package com.generalidevelopment.hit2013.web;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import com.generalidevelopment.hit2013.domain.Item;
import com.generalidevelopment.hit2013.repository.ItemRepository;
import com.generalidevelopment.hit2013.util.AppContext;
import com.generalidevelopment.hit2013.util.json.JSONResponseEnvelope;

@RequestMapping("/items")
@Controller
public class ItemController {

	@Autowired
	ItemRepository itemRepository;

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
	public String create(@Valid final Item item, final BindingResult bindingResult, final Model uiModel,
			final HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, item);
			return "items/create";
		}
		uiModel.asMap().clear();
		itemRepository.save(item);
		return "redirect:/items/" + encodeUrlPathSegment(item.getId().toString(), httpServletRequest);
	}

	@RequestMapping(value = "/new", produces = "text/html")
	public String createForm(final Model uiModel) {
		populateEditForm(uiModel, new Item());
		return "items/create";
	}

	@RequestMapping(value = "/{id}", produces = "text/html")
	public String show(@PathVariable("id") final Long id, final Model uiModel) {
		uiModel.addAttribute("item", itemRepository.findOne(id));
		uiModel.addAttribute("itemId", id);
		return "items/show";
	}

	@RequestMapping(produces = "text/html")
	public String list(final Model uiModel) {
		uiModel.addAttribute("items", itemRepository.findAll());

		return "items/list";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = "text/html")
	public String update(@Valid final Item item, final BindingResult bindingResult, final Model uiModel,
			final HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, item);
			return "items/update";
		}
		uiModel.asMap().clear();
		itemRepository.save(item);
		return "redirect:/items/" + encodeUrlPathSegment(item.getId().toString(), httpServletRequest);
	}

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
	public String updateForm(@PathVariable("id") final Long id, final Model uiModel) {
		populateEditForm(uiModel, itemRepository.findOne(id));
		return "items/update";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
	public String delete(@PathVariable("id") final Long id, final Model uiModel) {
		final Item item = itemRepository.findOne(id);
		itemRepository.delete(item);
		uiModel.asMap().clear();
		return "redirect:/items";
	}

	@RequestMapping(value = "/{id}/json", method = RequestMethod.DELETE, produces = "application/json")
	@ResponseBody
	public JSONResponseEnvelope deleteJSON(@PathVariable("id") final Long id, final Model uiModel) {
		final Item item = itemRepository.findOne(id);
		itemRepository.delete(item);
		uiModel.asMap().clear();
		return JSONResponseEnvelope.createSuccessResponseMsg(AppContext.getMsg("ui.message.delete.success"));
	}

	void populateEditForm(final Model uiModel, final Item item) {
		uiModel.addAttribute("item", item);
	}

	String encodeUrlPathSegment(String pathSegment, final HttpServletRequest httpServletRequest) {
		String enc = httpServletRequest.getCharacterEncoding();
		if (enc == null) {
			enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
		}
		try {
			pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
		} catch (final UnsupportedEncodingException uee) {
		}
		return pathSegment;
	}
}
