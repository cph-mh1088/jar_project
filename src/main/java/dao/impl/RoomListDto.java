package dao.impl;


import dto.RoomDto;

import java.util.List;

public record RoomListDto(int listSize, List<RoomDto> roomDtos) {}
