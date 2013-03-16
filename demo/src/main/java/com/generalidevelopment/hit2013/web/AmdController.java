package com.generalidevelopment.hit2013.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.generalidevelopment.hit2013.repository.ItemRepository;

@Controller
@RequestMapping("/amd")
public class AmdController {

	@Autowired
	private ItemRepository itemRepository;

	@RequestMapping(produces = "text/html")
	public String show(final Model uiModel) {
		uiModel.addAttribute("items", itemRepository.findAll());
		return "amd/list";
	}
}
