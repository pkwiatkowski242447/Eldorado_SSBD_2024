import {Button} from "@/components/ui/button"
import {Input} from "@/components/ui/input"
import {z} from "zod";
import {zodResolver} from "@hookform/resolvers/zod"
import {useForm} from "react-hook-form"
import {Form, FormControl, FormField, FormItem, FormMessage,} from "@/components/ui/form"
import {useTranslation} from "react-i18next";
import {useState} from "react";
import {Loader2} from "lucide-react";
import {
    AlertDialog,
    AlertDialogAction,
    AlertDialogCancel,
    AlertDialogContent,
    AlertDialogDescription, AlertDialogFooter, AlertDialogHeader,
    AlertDialogTitle
} from "@/components/ui/alert-dialog.tsx";
import {api} from "@/api/api.ts";
import handleApiError from "@/components/HandleApiError.ts";
import {toast} from "@/components/ui/use-toast.ts";

function EnterParkingWithReservationForm() {
    const {t} = useTranslation();
    const [isLoading, setIsLoading] = useState(false);
    const [enterValues, setEnterValues] = useState<{id:string}>({id:""});
    const [isDialogOpen, setIsDialogOpen] = useState(false);
    const [isResultDialogOpen, setIsResultDialogOpen] = useState(false);
    const formSchema = z.object({
        id: z.string().min(36, {message: "ZMIEN"})
            .max(36, {message: "ZMIEN"}).regex(RegExp("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}"), {message: "ZMIEN"}),
    })

    const form = useForm<z.infer<typeof formSchema>>({
        resolver: zodResolver(formSchema),
        defaultValues: {
            id: ""
        },
    })

    async function handleEnterReservation(){
        api.enterParkingWithReservation(enterValues.id)
            .then(() => {
                setIsDialogOpen(false);
                toast({
                    title: t("Success"),
                    description: t("You successfully exited the parking. Enjoy your stay.")
                });
            }).catch(error => {
                setIsDialogOpen(false);
                handleApiError(error);
            });
    }

    async function onSubmit(values: z.infer<typeof formSchema>) {
        setIsLoading(true);
        try {
            setEnterValues(values);
            setIsDialogOpen(true);
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
                    name="id"
                    render={({field}) => (
                        <FormItem>
                            <div className="grid gap-4">
                                <FormControl>
                                    <Input placeholder="25268941-b7ad-4288-b4bd-e0a3946b0bd8" {...field} />
                                </FormControl>
                                <FormMessage/>
                            </div>
                        </FormItem>
                    )}
                />
                <Button type="submit" className="mt-4" disabled={isLoading}>
                    {isLoading ? (
                        <>
                            <Loader2 className="mr-2 h-4 w-4 animate-spin"/>
                        </>
                    ) : (
                        t("Enter parking")
                    )}
                </Button>
            </form>
            <AlertDialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
                <AlertDialogContent>
                    <AlertDialogHeader>
                        <AlertDialogTitle>Enter Reservation</AlertDialogTitle>
                        <AlertDialogDescription>
                            Are you sure you want to enter the parking with this reservation ID?
                        </AlertDialogDescription>
                    </AlertDialogHeader>
                    <AlertDialogFooter>
                        <AlertDialogCancel onClick={() => setIsDialogOpen(false)}>No</AlertDialogCancel>
                        <AlertDialogAction onClick={handleEnterReservation}>Yes</AlertDialogAction>
                    </AlertDialogFooter>
                </AlertDialogContent>
            </AlertDialog>
            <AlertDialog open={isResultDialogOpen} onOpenChange={setIsResultDialogOpen}>
                <AlertDialogContent>
                    <AlertDialogHeader>
                        <AlertDialogTitle>Parking has been entered successfully!</AlertDialogTitle>
                        <AlertDialogDescription>
                            :)
                        </AlertDialogDescription>
                    </AlertDialogHeader>
                    <AlertDialogFooter>
                        <AlertDialogAction onClick={() => setIsResultDialogOpen(false)}>OK</AlertDialogAction>
                    </AlertDialogFooter>
                </AlertDialogContent>
            </AlertDialog>
        </Form>
    )
}

export default EnterParkingWithReservationForm
