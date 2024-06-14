import {Button} from "@/components/ui/button"
import {Input} from "@/components/ui/input"
import {z} from "zod";
import {zodResolver} from "@hookform/resolvers/zod"
import {useForm} from "react-hook-form"
import {Form, FormControl, FormField, FormItem, FormMessage,} from "@/components/ui/form"
import {FormLabel} from "react-bootstrap";
import {useTranslation} from "react-i18next";
import {Dispatch, SetStateAction, useEffect, useState} from "react";
import {Loader2} from "lucide-react";
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from "@/components/ui/select.tsx";
import {
    AlertDialog,
    AlertDialogAction,
    AlertDialogCancel,
    AlertDialogContent,
    AlertDialogDescription,
    AlertDialogTitle
} from "@/components/ui/alert-dialog.tsx";
import {ParkingType, sectorStrategy} from "@/types/Parking.ts";
import {api} from "@/api/api.ts";
import handleApiError from "@/components/HandleApiError.ts";

type editParkingFormProps = {
    setDialogOpen: Dispatch<SetStateAction<boolean>>
    refresh: () => void
    parkingId: string
}

function EditParkingForm({setDialogOpen, refresh, parkingId}:editParkingFormProps) {
    const {t} = useTranslation();
    const [isLoading, setIsLoading] = useState(false);
    const [isAlertDialogOpen, setAlertDialogOpen] = useState(false);
    const [editedParking, setEditedParking] = useState<ParkingType>({parkingId:"", version:"", city:"", street:"", zipCode:"", strategy:sectorStrategy.LEAST_OCCUPIED, signature:""})
    const [parking, setParking] = useState<ParkingType>({parkingId:"", version:"", city:"", street:"", zipCode:"", strategy:sectorStrategy.LEAST_OCCUPIED, signature:""});
    const formSchema = z.object({
        city: z.string().min(2, {message: t("edit.parking.form.city.too.short")})
            .max(50, {message: t("edit.parking.form.city.too.long")}).regex(RegExp("^([a-zA-Z\\u0080-\\u024F]+(?:. |-| |'))*[a-zA-Z\\u0080-\\u024F]*$"), {message: t("edit.parking.form.city.regex.not.met")}),
        street: z.string().min(2, {message: t("edit.parking.form.street.too.short")})
            .max(60, {message: t("edit.parking.form.street.too.long")}).regex(RegExp("^[A-Za-z0-9.-]{5,50}$"), {message: t("edit.parking.form.street.regex.not.met")}),
        zipCode: z.string().min(6, {message: t("edit.parking.form.zip.code.too.short")})
            .max(6, {message: t("edit.parking.form.zip.code.too.long")}).regex(RegExp("^\\d{2}-\\d{3}$"), {message: t("edit.parking.form.zip.code.regex.not.met")}),
        strategy: z.enum(["LEAST_OCCUPIED", "MOST_OCCUPIED", "LEAST_OCCUPIED_WEIGHTED"])
    })

    useEffect(() => {
        api.getParkingById(parkingId)
            .then((response) => {
                setParking({...response.data, signature: response.headers['etag']})
            })
            .catch(error => {
                handleApiError(error);
            });
    }, []);


    const form = useForm<z.infer<typeof formSchema>>({
        resolver: zodResolver(formSchema),
        defaultValues: {
            city: parking.city,
            street: parking.street,
            zipCode: parking.zipCode,
            strategy: parking.strategy
        },
    })

    useEffect(() => {
        console.log(parking);
        form.reset({
            city: parking.city,
            street: parking.street,
            zipCode: parking.zipCode,
            strategy: parking.strategy
        });
    }, [parking]);

    async function handleAlertDialog(){
        console.log(editedParking);
        api.modifyParking(editedParking)
            .then(() => {
                refresh();
                setAlertDialogOpen(false);
                setDialogOpen(false)})
            .catch(error => {
                setAlertDialogOpen(false);
                handleApiError(error);
            });
    }

    async function onSubmit(values: z.infer<typeof formSchema>) {
        setIsLoading(true);
        try {
            setEditedParking({
                parkingId: parking.parkingId,
                version: parking.version,
                city: values.city,
                street:values.street,
                zipCode:values.zipCode,
                strategy:sectorStrategy[values.strategy],
                signature: parking.signature
            })
            setAlertDialogOpen(true);
        } catch (error) {
            console.log(error);
        } finally {
            setIsLoading(false);
        }
    }

    return (
        <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
                <FormField
                    control={form.control}
                    name="city"
                    render={({field}) => (
                        <FormItem>
                            <div className="grid gap-4">
                                <FormLabel className="text-left">{t("edit.parking.form.city")}</FormLabel>
                                <FormControl>
                                    <Input placeholder="Warszawa" {...field} />
                                </FormControl>
                                <FormMessage/>
                            </div>
                        </FormItem>
                    )}
                />
                <FormField
                    control={form.control}
                    name="street"
                    render={({field}) => (
                        <FormItem>
                            <div className="grid gap-4">
                                <FormLabel className="text-left">{t("edit.parking.form.street")}</FormLabel>
                                <FormControl>
                                    <Input placeholder="Ziemniaczana" {...field} />
                                </FormControl>
                                <FormMessage/>
                            </div>
                        </FormItem>
                    )}
                />
                <FormField
                    control={form.control}
                    name="zipCode"
                    render={({field}) => (
                        <FormItem>
                            <div className="grid gap-4">
                                <FormLabel className="text-left">{t("edit.parking.form.zip.code")}</FormLabel>
                                <FormControl>
                                    <Input placeholder="11-111" {...field} />
                                </FormControl>
                                <FormMessage/>
                            </div>
                        </FormItem>
                    )}
                />
                <FormField
                    control={form.control}
                    name="strategy"
                    render={({field}) => (
                        <FormItem>
                            <div className="grid gap-4">
                                <FormLabel className="text-left">{t("edit.parking.form.strategy")}</FormLabel>
                                <Select onValueChange={field.onChange} value={field.value.toString()}>
                                    <FormControl>
                                        <SelectTrigger>
                                            <SelectValue placeholder={t( "create.parking.form.select.strategy")} />
                                        </SelectTrigger>
                                    </FormControl>
                                    <SelectContent>
                                        <SelectItem value="LEAST_OCCUPIED">{t("edit.parking.form.least.occupied")}</SelectItem>
                                        <SelectItem value="MOST_OCCUPIED">{t("edit.parking.form.most.occupied")}</SelectItem>
                                        <SelectItem value="LEAST_OCCUPIED_WEIGHTED">{t("edit.parking.form.least.occupied.weighted")}</SelectItem>
                                    </SelectContent>
                                </Select>
                            </div>
                        </FormItem>
                    )}
                />
                <Button type="submit" className="w-full" disabled={isLoading}>
                    {isLoading ? (
                        <>
                            <Loader2 className="mr-2 h-4 w-4 animate-spin"/>
                        </>
                    ) :
                        t("edit.parking.form.edit")
                    }
                </Button>
            </form>
            <AlertDialog open={isAlertDialogOpen} onOpenChange={setAlertDialogOpen}>
                <AlertDialogContent>
                    <AlertDialogTitle>{t("general.confirm")}</AlertDialogTitle>
                    <AlertDialogDescription>
                        {t("edit.parking.form.are.you.sure.you.want.to.edit.this.parking")}
                    </AlertDialogDescription>
                    <AlertDialogAction onClick={handleAlertDialog}>
                        {t("general.ok")}
                    </AlertDialogAction>
                    <AlertDialogCancel>{t("general.cancel")}</AlertDialogCancel>
                </AlertDialogContent>
            </AlertDialog>
        </Form>
    )
}

export default EditParkingForm
