import {
    Breadcrumb,
    BreadcrumbItem,
    BreadcrumbLink,
    BreadcrumbList,
    BreadcrumbSeparator
} from "@/components/ui/breadcrumb";
import {Slash} from "lucide-react";
import {useTranslation} from "react-i18next";
import {useParams} from "react-router-dom";
import {api} from "@/api/api";
import {CreateReservationForm} from "@/components/forms/CreateReservationForm.tsx";
import handleApiError from "@/components/HandleApiError.ts";
import {toast} from "@/components/ui/use-toast.ts";
import {useEffect, useState} from "react";
import {SectorInfoType} from "@/types/Parking.ts";
import {Button} from "@/components/ui/button.tsx";
import {Table, TableBody, TableCell, TableHead, TableHeader, TableRow} from "@/components/ui/table.tsx";
import {Badge} from "@/components/ui/badge.tsx";

function CreateReservationInSectorPage() {
    const {t} = useTranslation();
    const {id} = useParams<{ id: string }>();
    const [sectorInfo, setSectorInfo] = useState<SectorInfoType>();

    const handleReservationSubmit = async (beginTime: Date, endTime: Date) => {
        if (id) {
            await api.createReservation(id, beginTime.toISOString(), endTime.toISOString()).then(() => {
                toast({
                    title: "Reservation created successfully!",
                    description: "To see the reservation details, go to the My Reservations page.",
                });
            }).catch((error) => {
                handleApiError(error);
            });
        }
    };

    const fetchSectorInfo = (sectorId: string) => {
        if (sectorId) {
            api.getSectorInfo(sectorId)
                .then(response => {
                    setSectorInfo(response.data);
                })
                .catch(error => {
                    handleApiError(error);
                });
        }
    };

    useEffect(() => {
        if (id) {
            fetchSectorInfo(id);
        }
    }, []);

    return (
        <div className="flex min-h-screen w-full flex-col">
            <div className="flex justify-between items-center pt-2">
                <Breadcrumb className="pl-2">
                    <BreadcrumbList>
                        <BreadcrumbItem>
                            <BreadcrumbLink href="/home">{t("breadcrumb.home")}</BreadcrumbLink>
                        </BreadcrumbItem>
                        <BreadcrumbSeparator>
                            <Slash/>
                        </BreadcrumbSeparator>
                        <BreadcrumbItem>
                            <BreadcrumbLink href="/parking-list">{"Active Parking List"}</BreadcrumbLink>
                        </BreadcrumbItem>
                        <BreadcrumbSeparator>
                            <Slash/>
                        </BreadcrumbSeparator>
                        <BreadcrumbItem>
                            <BreadcrumbLink>{"Create a reservation"}</BreadcrumbLink>
                        </BreadcrumbItem>
                    </BreadcrumbList>
                </Breadcrumb>
                <Button variant={"ghost"} disabled={true}/>
            </div>
            <div className="mx-auto grid w-full max-w-6xl gap-2 p-10">
                <h1 className="text-3xl font-semibold">Create a reservation</h1>
                <div>
                    <Table className="p-10 flex-grow mt-2">
                        <TableHeader>
                            <TableRow className={"text-center p-10"}>
                                <TableHead
                                    className="text-center">{t("Name")}</TableHead>
                                <TableHead
                                    className="text-center">{t("Type")}</TableHead>
                                <TableHead
                                    className="text-center">{t("Max places")}</TableHead>
                            </TableRow>
                        </TableHeader>
                        <TableBody className={"text-center"}>
                            <TableRow key={sectorInfo?.id} className="flex-auto">
                                <TableCell>{sectorInfo?.name}</TableCell>
                                <TableCell><Badge variant={"default"}>{sectorInfo?.type} </Badge></TableCell>
                                <TableCell>{sectorInfo?.maxPlaces}</TableCell>
                            </TableRow>
                        </TableBody>
                    </Table>
                </div>
                <div className={"pt-5 flex justify-center"}>
                    <CreateReservationForm onSubmit={handleReservationSubmit}/>
                </div>
            </div>
        </div>
    );
}

// @ts-expect-error ????
export default CreateReservationInSectorPage;