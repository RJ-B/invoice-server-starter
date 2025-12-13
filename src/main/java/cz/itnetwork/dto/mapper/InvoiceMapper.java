package cz.itnetwork.dto.mapper;

import cz.itnetwork.dto.Invoice.InvoiceDTO;
import cz.itnetwork.dto.Invoice.InvoiceReadDTO;
import cz.itnetwork.entity.Invoice;
import org.mapstruct.Mapper;

/**
 * MapStruct mapper zodpovědný za převod mezi entitou Invoice
 * a odpovídajícími datovými přenosovými objekty (DTO).
 *
 * Mapper slouží výhradně k transformaci dat a neobsahuje
 * žádnou aplikační ani validační logiku.
 */
@Mapper(componentModel = "spring", uses = {PersonMapper.class})
public interface InvoiceMapper {

    /**
     * Převod datového přenosového objektu faktury na entitu.
     *
     * @param source vstupní datový přenosový objekt typu InvoiceDTO
     * @return entita Invoice vytvořená z DTO
     */
    Invoice toEntity(InvoiceDTO source);

    /**
     * Převod entity faktury na datový přenosový objekt.
     *
     * @param source entita Invoice
     * @return datový přenosový objekt InvoiceDTO
     */
    InvoiceDTO toDto(Invoice source);

    /**
     * Převod entity faktury na read-only DTO určené pro výstupní data.
     *
     * @param source entita Invoice
     * @return datový přenosový objekt InvoiceReadDTO
     */
    InvoiceReadDTO toReadDTO(Invoice source);
}
