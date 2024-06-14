import {useEffect, useState} from 'react';
import {api} from "@/api/api.ts";
import {
    Breadcrumb,
    BreadcrumbItem,
    BreadcrumbLink,
    BreadcrumbList,
    BreadcrumbSeparator
} from '@/components/ui/breadcrumb.tsx';
import {
    Pagination,
    PaginationContent,
    PaginationItem,
    PaginationLink,
    PaginationNext,
    PaginationPrevious
} from "@/components/ui/pagination.tsx";
import {useTranslation} from "react-i18next";
import handleApiError from "@/components/HandleApiError.ts";
import {Badge} from "@/components/ui/badge.tsx";
import {Loader2, Slash} from "lucide-react";
import {Button} from "@/components/ui/button.tsx";
import {ParkingListType} from "@/types/Parking.ts";
import {Table, TableBody, TableCell, TableHead, TableHeader, TableRow} from "@/components/ui/table.tsx";
import {useNavigate} from "react-router-dom";
import {Pathnames} from "@/router/pathnames.ts";

function ActiveParkingPage() {
    // @ts-expect-error no time
    const [currentPage, setCurrentPage] = useState(() => parseInt(0));
    const [parking, setParking] = useState<ParkingListType[]>([]);
    const {t} = useTranslation();
    const [isRefreshing, setIsRefreshing] = useState(false);
    const [isSubmitClicked, setIsSubmitClicked] = useState(false);
    const pageSize = 5;
    const navigate = useNavigate();

    const fetchParking = (page?: number) => {
        const actualPage = page != undefined ? page : currentPage;
        const details = `?pageNumber=${actualPage}&pageSize=${pageSize}`;

        api.getActiveParking(details)
            .then(response => {
                if (response.status === 200) {
                    setCurrentPage(actualPage);
                    setParking(response.data);
                }
            })
            .catch(error => {
                handleApiError(error);
            });
    };

    useEffect(() => {
        fetchParking();
        setIsSubmitClicked(false);
    }, [isSubmitClicked]);

    function refresh() {
        setIsRefreshing(true);
        fetchParking();
        setTimeout(() => setIsRefreshing(false), 1000);
    }

    return (
        <div className="flex min-h-screen w-full flex-col">
            <div className="flex justify-between items-center pt-2">
                <Breadcrumb className={"pl-2"}>
                    <BreadcrumbList>
                        <BreadcrumbItem>
                            <BreadcrumbLink className="cursor-pointer" onClick={() => navigate(Pathnames.public.home)}>{t("breadcrumb.home")}</BreadcrumbLink>
                        </BreadcrumbItem>
                        <BreadcrumbSeparator>
                            <Slash/>
                        </BreadcrumbSeparator>
                        <BreadcrumbItem>
                            <BreadcrumbLink>{t("breadcrumb.active.parking.list")}</BreadcrumbLink>
                        </BreadcrumbItem>
                    </BreadcrumbList>
                </Breadcrumb>
                <Button onClick={refresh} variant={"ghost"} className="w-auto" disabled={isRefreshing}>
                    {isRefreshing ? (
                        <>
                            <Loader2 className="mr-2 h-4 w-4 animate-spin"/>
                        </>
                    ) : (
                        t("general.refresh")
                    )}
                </Button>
            </div>
            <div className={"pt-5"}>
                <Table className="p-10 flex-grow">
                    <TableHeader>
                        <TableRow className={"text-center p-10"}>
                            <TableHead
                                className="text-center">{t("active.parking.page.city")}</TableHead>
                            <TableHead
                                className="text-center">{t("active.parking.page.street")}</TableHead>
                            <TableHead
                                className="text-center">{t("active.parking.page.zip.code")}</TableHead>
                            <TableHead
                                className="text-center">{t("active.parking.page.sector.types")}</TableHead>
                            <TableHead className="text-center"></TableHead>
                        </TableRow>
                    </TableHeader>
                    <TableBody className={"text-center"}>
                        {parking.map(parking => (
                            <TableRow key={parking.id} className="flex-auto">
                                <TableCell>{parking.city}</TableCell>
                                <TableCell>{parking.street}</TableCell>
                                <TableCell>{parking.zipCode}</TableCell>
                                <TableCell>
                                    {parking.sectorTypes.map(level => {
                                        return <Badge key={level} variant={"secondary"}>{t(level)} </Badge>;
                                    })}
                                </TableCell>
                                <TableCell>
                                    <Button variant={"default"} onClick={() => {
                                        navigate(`/parking-list/parking-info/${parking.id}`)
                                    }}>{t("active.parking.page.view.info")}</Button>
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
                <div className={"pt-5"}>
                    <Pagination>
                        <PaginationContent>
                            <PaginationItem>
                                <PaginationPrevious
                                    onClick={() => {
                                        if (currentPage > 0) fetchParking(currentPage - 1)
                                    }}
                                />
                            </PaginationItem>
                            <PaginationItem>
                                <PaginationLink>{currentPage + 1}</PaginationLink>
                            </PaginationItem>
                            <PaginationItem>
                                <PaginationNext
                                    onClick={() => {
                                        if (parking.length === pageSize) fetchParking(currentPage + 1)
                                    }}
                                />
                            </PaginationItem>
                        </PaginationContent>
                    </Pagination>
                </div>
            </div>
        </div>
    );
}

export default ActiveParkingPage;
